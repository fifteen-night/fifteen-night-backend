package com.fn.eureka.client.deliveryservice.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fn.common.global.dto.CommonPageResponse;
import com.fn.common.global.exception.CustomApiException;
import com.fn.eureka.client.deliveryservice.application.dto.delivery.request.CreateDeliveryRequestDto;
import com.fn.eureka.client.deliveryservice.application.dto.delivery.request.UpdateDeliveryRequestDto;
import com.fn.eureka.client.deliveryservice.application.dto.delivery.response.CreateDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.dto.delivery.response.GetAllDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.dto.delivery.response.GetDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.dto.delivery.response.UpdateDeliveryResponseDto;
import com.fn.eureka.client.deliveryservice.application.dto.deliveryRoute.request.CreateDeliveryRouteRequestDto;
import com.fn.eureka.client.deliveryservice.application.dto.deliveryRouteSequence.request.CreateSequenceRequestDto;
import com.fn.eureka.client.deliveryservice.application.dto.deliveryRouteSequence.request.UpdateSequenceRequestDto;
import com.fn.eureka.client.deliveryservice.application.service.DeliveryService;
import com.fn.eureka.client.deliveryservice.domain.model.delivery.Delivery;
import com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute.DeliveryRoute;
import com.fn.eureka.client.deliveryservice.domain.model.deliveryRoute.DeliveryRouteStatus;
import com.fn.eureka.client.deliveryservice.domain.model.deliveryRouteSequence.DeliveryRouteSequence;
import com.fn.eureka.client.deliveryservice.domain.model.route.HubToHub;
import com.fn.eureka.client.deliveryservice.domain.repository.DeliveryRepository;
import com.fn.eureka.client.deliveryservice.domain.repository.DeliveryRouteRepository;
import com.fn.eureka.client.deliveryservice.domain.repository.DeliveryRouteSequenceRepository;
import com.fn.eureka.client.deliveryservice.domain.repository.HubToHubRepository;
import com.fn.eureka.client.deliveryservice.domain.util.Node;
import com.fn.eureka.client.deliveryservice.domain.util.PqFormat;
import com.fn.eureka.client.deliveryservice.domain.util.TimeUtils;
import com.fn.eureka.client.deliveryservice.exception.DeliveryException;
import com.fn.eureka.client.deliveryservice.infrastructure.client.DeliveryManagerServiceClient;
import com.fn.eureka.client.deliveryservice.infrastructure.client.HubServiceClient;
import com.fn.eureka.client.deliveryservice.presentation.dto.response.HubClientResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

	private final DeliveryRepository deliveryRepository;
	private final DeliveryRouteRepository deliveryRouteRepository;
	private final DeliveryRouteSequenceRepository deliveryRouteSequenceRepository;
	private final HubToHubRepository hubToHubRepository;
	private final HubServiceClient hubServiceClient;
	private final DeliveryManagerServiceClient deliveryManagerServiceClient;

	@Override
	@Transactional
	public CreateDeliveryResponseDto createDelivery(CreateDeliveryRequestDto createDeliveryRequestDto) {

		// 1. 주문이 들어오면 자동으로 배송 생성
		UUID deliveryManagerId = deliveryManagerServiceClient.findCompanyDeliver(
			createDeliveryRequestDto.getDepartureHubId());

		Delivery delivery = CreateDeliveryRequestDto.toDelivery(createDeliveryRequestDto, deliveryManagerId);

		if (deliveryRepository.existsByOrderIdAndIsDeletedIsFalse(delivery.getOrderId())) {
			throw new CustomApiException(DeliveryException.ALREADY_EXISTS_DELIVERY);
		}

		Delivery savedDelivery = deliveryRepository.save(delivery);

		// 2. 배송이 생성되면 자동으로 배송 루트 생성
		DeliveryRoute deliveryRoute = CreateDeliveryRouteRequestDto.toDeliveryRoute(delivery);
		DeliveryRoute savedDeliveryRoute = deliveryRouteRepository.save(deliveryRoute);

		// 3. 배송루트가 생성이 되면 다익스트라 알고리즘을 통해 시퀀스를 생성
		boolean iscreate = true;
		List<DeliveryRouteSequence> deliveryRouteSequences = startAlgorithm(savedDeliveryRoute, iscreate);

		// 4. 생성된 시퀀스를 배송 루트에 업데이트
		int totalTime = 0;
		BigDecimal totalDistance = BigDecimal.ZERO;

		for (DeliveryRouteSequence deliveryRouteSequence : deliveryRouteSequences) {
			totalTime += TimeUtils.convertMilliseconds(deliveryRouteSequence.getQuantity());
			totalDistance = totalDistance.add(deliveryRouteSequence.getDistance());
		}

		savedDeliveryRoute.updateDeliverySequence(
			deliveryRouteSequences,
			TimeUtils.convertTime(totalTime),
			totalDistance,
			DeliveryRouteStatus.WAITING);

		// 5. 값 리턴
		return CreateDeliveryResponseDto.fromDelivery(savedDelivery, savedDeliveryRoute);
	}

	@Override
	public GetDeliveryResponseDto searchOneDelivery(UUID deliveryId) {

		Delivery targetDelivery = findDeliveryById(deliveryId);

		return GetDeliveryResponseDto.fromDelivery(targetDelivery);
	}

	@Override
	public CommonPageResponse<GetAllDeliveryResponseDto> searchAllDelivery(Pageable pageable) {

		Page<Delivery> deliveries = deliveryRepository.findAllByIsDeletedIsFalse(pageable);

		if (deliveries.isEmpty()) {
			throw new CustomApiException(DeliveryException.DELIVERY_NOT_FOUND);
		}

		Page<GetAllDeliveryResponseDto> getAllDeliveryResponseDtos = deliveries.map(
			GetAllDeliveryResponseDto::fromDelivery);

		return new CommonPageResponse<>(getAllDeliveryResponseDtos);
	}

	@Override
	@Transactional
	public void deleteDelivery(UUID deliveryId) {

		Delivery targetDelivery = findDeliveryById(deliveryId);

		targetDelivery.markAsDeleted();

		DeliveryRoute targetDeliveryRoute = deliveryRouteRepository.findByDeliveryAndIsDeletedIsFalse(
				targetDelivery)
			.orElseThrow(() -> new CustomApiException(DeliveryException.DELIVERY_ROUTE_NOT_FOUND));

		targetDeliveryRoute.markAsDeleted();

		List<DeliveryRouteSequence> sequences = deliveryRouteSequenceRepository.findBySequenceIdAndIsDeletedIsFalse(
			targetDeliveryRoute.getDeliveryRouteId());

		for (DeliveryRouteSequence sequence : sequences) {
			sequence.markAsDeleted();
		}

	}

	@Override
	@Transactional
	public UpdateDeliveryResponseDto updateDelivery(UUID deliveryId,
		UpdateDeliveryRequestDto updateDeliveryRequestDto) {

		Delivery targetDelivery = findDeliveryById(deliveryId);

		boolean isChanged = validateChangeLocation(updateDeliveryRequestDto, targetDelivery);

		targetDelivery.update(updateDeliveryRequestDto);

		DeliveryRoute targetDeliveryRoute = deliveryRouteRepository.findByDeliveryAndIsDeletedIsFalse(
				targetDelivery)
			.orElseThrow(() -> new CustomApiException(DeliveryException.DELIVERY_ROUTE_NOT_FOUND));

		targetDeliveryRoute.update(targetDelivery);

		// 1. targetDeliveryRoute를 일단 업데이트 한다. 밑에서 사용

		// 2. 두개를 다 업데이트 했는데 , 문제가 발생할 수 도 있음 하지만 , 이상태에서는 db가 실제로 업데이트가 안된상태이다.
		// 쓰기 지연저장소 저장 되어있다가 쿼리 날라감
		// 쿼리 순서 문제가 발생할 경우에는 , flush() 수동으로 update() 이후 , 왠만한 경우에는 문제가 없을 것 같다.

		List<DeliveryRouteSequence> deliveryRouteSequences = null;

		int totalTime = 0;
		BigDecimal totalDistance = BigDecimal.ZERO;

		if (isChanged) {
			boolean iscreate = false;
			deliveryRouteSequences = startAlgorithm(targetDeliveryRoute, iscreate);
			for (DeliveryRouteSequence deliveryRouteSequence : deliveryRouteSequences) {
				totalTime += TimeUtils.convertMilliseconds(deliveryRouteSequence.getQuantity());
				totalDistance = totalDistance.add(deliveryRouteSequence.getDistance());
			}
		}

		targetDeliveryRoute.updateDeliverySequence(
			deliveryRouteSequences,
			TimeUtils.convertTime(totalTime),
			totalDistance,
			updateDeliveryRequestDto.getDeliveryRouteStatus()
		);

		return UpdateDeliveryResponseDto.fromDelivery(targetDelivery, targetDeliveryRoute);
	}

	private boolean validateChangeLocation(UpdateDeliveryRequestDto updateDeliveryRequestDto, Delivery targetDelivery) {
		/*
			변경되면 T 안되면 F
			출발 T 도착 T -> T
			출발 T 도착 F -> T
			출발 F 도착 F -> T
			출발 F 도착 F -> F
		 */
		return !updateDeliveryRequestDto.getDepartureHubId().equals(targetDelivery.getDepartureHubId()) ||
			!updateDeliveryRequestDto.getDestinationHubId().equals(targetDelivery.getDestinationHubId());
	}

	private Delivery findDeliveryById(UUID deliveryId) {

		Delivery targetDelivery = deliveryRepository.findByDeliveryIdAndIsDeletedIsFalse(deliveryId)
			.orElseThrow(() -> new CustomApiException(DeliveryException.DELIVERY_NOT_FOUND));

		return targetDelivery;
	}

	private String findHubName(UUID hubId) {
		HubClientResponseDto hubResponse = hubServiceClient.findHub(hubId);

		return hubResponse.getData().getHubAddress();
	}

	private List<DeliveryRouteSequence> startAlgorithm(DeliveryRoute deliveryRoute, boolean isCreate) {

		UUID departureHubId = deliveryRoute.getDepartureHubAddress();
		UUID destinationHubId = deliveryRoute.getDestinationHubAddress();

		String departureHubName = findHubName(departureHubId);
		String destinationHubName = findHubName(destinationHubId);

		// 모든 허브 루트 불러오기
		List<HubToHub> hubRoutes = hubToHubRepository.findAllByIsDeletedIsFalse();

		// 해당루트정보로 그래프 연결
		Map<String, List<Node>> graph = createGraph(hubRoutes);

		// 그래프를 가지고 다익스트라 최단경로 알고리즘 수행
		List<String> result = dijkstra(departureHubName, destinationHubName, graph);

		log.info("result: {}", result);

		// 결과값을 DeliveryRouteSequence db에 저장
		List<DeliveryRouteSequence> deliveryRouteSequences = new ArrayList<>();

		for (int i = 1; i <= result.size() - 1; i++) {

			for (HubToHub hubRoute : hubRoutes) {

				if (hubRoute.getDepartureHubAddress().equals(result.get(i - 1)) &&
					hubRoute.getArrivalHubAddress().equals(result.get(i))) {

					if (isCreate) {

						CreateSequenceRequestDto sequence = CreateSequenceRequestDto.builder()
							.deliveryRoute(deliveryRoute)
							.sequenceNumber(i)
							.departureHubAddress(hubRoute.getDepartureHubAddress())
							.arrivalHubAddress(hubRoute.getArrivalHubAddress())
							.quantity(hubRoute.getHthQuantity())
							.distance(hubRoute.getHthDistance())
							.build();

						UUID hubDeliver = deliveryManagerServiceClient.findHubDeliver();

						DeliveryRouteSequence deliveryRouteSequence = CreateSequenceRequestDto.toSequence(sequence,
							hubDeliver);

						deliveryRouteSequenceRepository.save(deliveryRouteSequence);

						deliveryRouteSequences.add(deliveryRouteSequence);
					} else {

						DeliveryRouteSequence existingSequence = deliveryRouteSequenceRepository
							.findByDeliveryRouteAndSequenceNumber(deliveryRoute, i)
							.orElseThrow(
								() -> new CustomApiException(DeliveryException.DELIVERY_ROUTE_SEQUENCE_NOT_FOUND));

						UpdateSequenceRequestDto updateSequence = UpdateSequenceRequestDto.builder()
							.deliveryRoute(deliveryRoute)
							.sequenceNumber(i)
							.departureHubAddress(hubRoute.getDepartureHubAddress())
							.arrivalHubAddress(hubRoute.getArrivalHubAddress())
							.quantity(hubRoute.getHthQuantity())
							.distance(hubRoute.getHthDistance())
							.build();

						existingSequence.update(updateSequence);

						deliveryRouteSequences.add(existingSequence);
					}

					break;
				}
			}
		}

		return deliveryRouteSequences;
	}

	private Map<String, List<Node>> createGraph(List<HubToHub> hubRoutes) {

		Map<String, List<Node>> graph = new HashMap<>();
		for (HubToHub hubRoute : hubRoutes) {
			// 일단은 출발지 도착지를 허브 주소로 저장
			graph.computeIfAbsent(hubRoute.getDepartureHubAddress(), address -> new ArrayList<>())
				.add(new Node(hubRoute.getArrivalHubAddress(),
					// db저장이 LocalTime이라 다시 millsec으로 변환하여 가중치를 저장
					TimeUtils.convertMilliseconds(hubRoute.getHthQuantity())));
		}

		return graph;
	}

	private List<String> dijkstra(String start, String goal, Map<String, List<Node>> graph) {
		// 노드(허브들)
		List<String> vertex = new ArrayList<>(graph.keySet());
		// 시간값
		Map<String, Integer> time = new HashMap<>();
		// 경로
		Map<String, String> saveRoute = new HashMap<>();
		// 방문여부
		Set<String> visited = new HashSet<>();

		// 모든 노드에 대해 거리값을 초기화
		// 시작점을 제외한 나머지 노드들 거리를 최대로 초기화
		// 시작점은 0으로 설정
		for (String v : vertex) {
			time.put(v, Integer.MAX_VALUE);
			saveRoute.put(v, null);
		}
		time.put(start, 0);

		// 우선순위 큐 (최소 거리 기반으로 처리)
		PriorityQueue<PqFormat> pq = new PriorityQueue<>(Comparator.comparingInt(PqFormat::getTime));
		pq.add(new PqFormat(start, 0));

		while (!pq.isEmpty()) {
			PqFormat current = pq.poll();
			String currentNode = current.getIndex();
			int currentDistance = current.getTime();

			// 방문한 노드면 skip
			if (visited.contains(currentNode)) {
				continue;
			}
			visited.add(currentNode);

			// 인접 노드들 처리
			if (graph.containsKey(currentNode)) {

				for (Node neighbor : graph.get(currentNode)) {

					if (!visited.contains(neighbor.getNextHubName())) {

						int newDist = currentDistance + neighbor.getTimeCost();

						if (newDist < time.get(neighbor.getNextHubName())) {
							time.put(neighbor.getNextHubName(), newDist);
							saveRoute.put(neighbor.getNextHubName(), currentNode);
							pq.add(new PqFormat(neighbor.getNextHubName(), newDist));
						}
					}
				}
			}
		}

		return createPath(start, goal, saveRoute);
	}

	private List<String> createPath(String start, String goal, Map<String, String> saveRoute) {

		List<String> path = new ArrayList<>();
		String current = goal;

		while (current != null) {
			path.add(current);

			// current 문자열과 map에 저장되어있는 문자열의 해시값이 달라서 null 값을 반환하므로 for문돌려서 equlas로 비교
			// current와 saveRoute의 키가 동일한지 실제로 비교
			String previousNode = null;
			for (String key : saveRoute.keySet()) {
				if (key.equals(current)) {
					previousNode = saveRoute.get(key);
					break;
				}
			}

			// 경로가 더 이상 없으면 종료
			if (previousNode == null) {
				break;
			}

			current = previousNode;
		}

		if (path.isEmpty() || !path.contains(start)) {
			return Collections.emptyList();
		}

		// 경로를 반대로 출력
		Collections.reverse(path);
		return path;
	}

}
