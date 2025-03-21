INSERT INTO company.p_company (
    company_id, company_name, company_address, company_type,
    company_hub_id, company_manager_id, created_at, updated_at, is_deleted
) VALUES
      ('4cc3db25-c8d0-4676-b334-f642607727b7', '테크코프', '서울특별시 강남구 테헤란로 123', 'SUPPLIER',
       '550e8400-e29b-41d4-a716-446655440000', '123e4567-e89b-12d3-a456-426614174000', NOW(), NOW(), false),

      ('6fa459ea-ee8a-3ca4-894e-db77e160355e', '이노비즈', '서울특별시 마포구 독막로 45', 'RECEIVER',
       '550e8400-e29b-41d4-a716-446655440000', '223e4567-e89b-12d3-a456-426614174001', NOW(), NOW(), false),

      ('7ab4a8e4-ffcc-4d8f-8b13-f2c4a62f82e1', '스마트소프트', '경기도 성남시 분당구 불정로 90', 'SUPPLIER',
       '550e8400-e29b-41d4-a716-446655440000', '323e4567-e89b-12d3-a456-426614174002', NOW(), NOW(), false),

      ('8cd5b0a6-1c3d-432d-a8ff-7f3263e412f9', '넥스트테크', '부산광역시 해운대구 센텀중앙로 78', 'RECEIVER',
       '550e8400-e29b-41d4-a716-446655440000', '423e4567-e89b-12d3-a456-426614174003', NOW(), NOW(), false),

      ('9ea8b6f4-32e2-4e8d-987b-df45ac98c777', '퓨처네트', '대구광역시 동구 동대구로 231', 'SUPPLIER',
       '550e8400-e29b-41d4-a716-446655440000', '523e4567-e89b-12d3-a456-426614174004', NOW(), NOW(), false),

      ('aa3f8a0b-5934-4b5d-873c-32c3db5fcd1f', '데이터웨이브', '서울특별시 서초구 서초대로 77', 'RECEIVER',
       '660e8400-e29b-41d4-a716-446655440001', '623e4567-e89b-12d3-a456-426614174005', NOW(), NOW(), false),

      ('bb47cfa8-1d9f-4e6d-97c6-d7f48c1e9b22', '클라우드엣지', '인천광역시 남동구 논현로 102', 'SUPPLIER',
       '660e8400-e29b-41d4-a716-446655440001', '723e4567-e89b-12d3-a456-426614174006', NOW(), NOW(), false),

      ('cc5e2bb7-2f08-42c9-9fb7-4b4f5e56cdd3', '에이아이솔루션', '광주광역시 북구 첨단과기로 88', 'RECEIVER',
       '770e8400-e29b-41d4-a716-446655440002', '823e4567-e89b-12d3-a456-426614174007', NOW(), NOW(), false),

      ('dd6f9a4e-4847-4f8b-937e-6f7a0c6a56a1', '블록체인랩스', '대전광역시 유성구 대학로 99', 'SUPPLIER',
       '770e8400-e29b-41d4-a716-446655440002', '923e4567-e89b-12d3-a456-426614174008', NOW(), NOW(), false),

      ('ee789c62-5c1d-44d4-9dc1-3a4f58e63bb5', '시큐어테크', '울산광역시 남구 삼산로 55', 'RECEIVER',
       '880e8400-e29b-41d4-a716-446655440003', 'a23e4567-e89b-12d3-a456-426614174009', NOW(), NOW(), false);
