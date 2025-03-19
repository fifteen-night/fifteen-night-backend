INSERT INTO product.p_product (
    product_id, product_name, product_company_id, product_quantity,
    created_at, updated_at, is_deleted, deleted_at,
    created_by, updated_by, deleted_by
) VALUES
      ('a1b2c3d4-e5f6-4789-abcd-123456789abc', '스마트폰 A', '4cc3db25-c8d0-4676-b334-f642607727b7', 100, NOW(), NOW(), false, NULL, '123e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174000', NULL),
      ('b2c3d4e5-f6a7-4890-bcde-23456789abcd', '노트북 B', '6fa459ea-ee8a-3ca4-894e-db77e160355e', 50, NOW(), NOW(), false, NULL, '223e4567-e89b-12d3-a456-426614174001', '223e4567-e89b-12d3-a456-426614174001', NULL),
      ('c3d4e5f6-a7b8-4901-cdef-3456789abcde', '태블릿 C', '7ab4a8e4-ffcc-4d8f-8b13-f2c4a62f82e1', 75, NOW(), NOW(), false, NULL, '323e4567-e89b-12d3-a456-426614174002', '323e4567-e89b-12d3-a456-426614174002', NULL),
      ('d4e5f6a7-b8c9-4012-def0-456789abcdef', '스마트워치 D', '8cd5b0a6-1c3d-432d-a8ff-7f3263e412f9', 120, NOW(), NOW(), false, NULL, '423e4567-e89b-12d3-a456-426614174003', '423e4567-e89b-12d3-a456-426614174003', NULL),
      ('e5f6a7b8-c9d0-4123-ef01-56789abcdef0', '데스크탑 E', '9ea8b6f4-32e2-4e8d-987b-df45ac98c777', 30, NOW(), NOW(), false, NULL, '523e4567-e89b-12d3-a456-426614174004', '523e4567-e89b-12d3-a456-426614174004', NULL),
      ('f6a7b8c9-d0e1-4234-f012-6789abcdef01', '모니터 F', 'aa3f8a0b-5934-4b5d-873c-32c3db5fcd1f', 90, NOW(), NOW(), false, NULL, '623e4567-e89b-12d3-a456-426614174005', '623e4567-e89b-12d3-a456-426614174005', NULL),
      ('a7b8c9d0-e1f2-4345-0123-789abcdef012', '헤드셋 G', 'bb47cfa8-1d9f-4e6d-97c6-d7f48c1e9b22', 200, NOW(), NOW(), false, NULL, '723e4567-e89b-12d3-a456-426614174006', '723e4567-e89b-12d3-a456-426614174006', NULL),
      ('b8c9d0e1-f2a3-4456-1234-89abcdef0123', '키보드 H', 'cc5e2bb7-2f08-42c9-9fb7-4b4f5e56cdd3', 150, NOW(), NOW(), false, NULL, '823e4567-e89b-12d3-a456-426614174007', '823e4567-e89b-12d3-a456-426614174007', NULL),
      ('c9d0e1f2-a3b4-4567-2345-9abcdef01234', '마우스 I', 'dd6f9a4e-4847-4f8b-937e-6f7a0c6a56a1', 180, NOW(), NOW(), false, NULL, '923e4567-e89b-12d3-a456-426614174008', '923e4567-e89b-12d3-a456-426614174008', NULL),
      ('d0e1f2a3-b4c5-4678-3456-abcdef012345', '외장하드 J', 'ee789c62-5c1d-44d4-9dc1-3a4f58e63bb5', 80, NOW(), NOW(), false, NULL, 'a23e4567-e89b-12d3-a456-426614174009', 'a23e4567-e89b-12d3-a456-426614174009', NULL);
