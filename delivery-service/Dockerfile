# Eclipse Temurin JDK 17(자바 17 지원)을 기반 이미지로 사용
FROM eclipse-temurin:17-jdk

#모든 작업이 /app 디렉토리에서 수행됨
WORKDIR /app

# 컨테이너에서 Gradle 을 사용할 수 있게 복사
COPY gradlew /app/
COPY gradle /app/gradle/
COPY build.gradle settings.gradle /app/

# build.gradle 메인 클래스 지정
RUN sed -i '/springBoot {/a \ \ mainClass = "com.fn.eureka.client.deliveryservice.DeliveryServiceApplication"' /app/build.gradle

# 공통모듈과 배달서비스 전체 복사
COPY common /app/common/
COPY delivery-service /app/delivery-service/

# 권한 수정
RUN chmod +x /app/gradlew

# 확인 로그용
RUN /bin/sh -c "ls -l /app/gradlew"  # 확인용, gradlew가 있는지 확인

# 애플리케이션 빌드(테스트 제외)
RUN /app/gradlew build -x test --stacktrace

# jar파일 복사
RUN cp /app/delivery-service/build/libs/delivery-service-0.0.1-SNAPSHOT-plain.jar /app.jar

# 컨테이너 내부 포트 설정
EXPOSE 8080

# 애플리케이션 실행
CMD ["java", "-jar", "/app.jar"]