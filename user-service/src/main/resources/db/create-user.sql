CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

INSERT INTO "user".p_user (
    user_id, is_deleted, created_at, user_name, user_password, user_nickname, user_slack_id, user_role, user_phone, user_email
) VALUES
      ('fd4e1f9c-bf89-4de3-8102-9f2ff3cf6d46', FALSE, NOW(), 'master', crypt('Test@1234', gen_salt('bf')), '마스터', 'U12345678', 'MASTER', '01011112222', 'master@example.com'),
      ('fd4e1f9c-bf89-4de3-8102-9f2ff3cf6d47', FALSE, NOW(), 'hubmng', crypt('Test@1234', gen_salt('bf')), '허브매니저', 'U87654321', 'HUB_MANAGER', '01022223333', 'hubmanager@example.com'),
      ('fd4e1f9c-bf89-4de3-8102-9f2ff3cf6d49', FALSE, NOW(), 'deliverymng', crypt('Test@1234', gen_salt('bf')), '배송매니저', 'U56781234', 'DELIVERY_MANAGER', '01033334444', 'deliverymgr@example.com'),
      ('3cf8a4ed-e2a1-4f36-89c4-44e50140f8a5', FALSE, NOW(), 'companymng', crypt('Test@1234', gen_salt('bf')), '회사매니저', 'U98765432', 'COMPANY_MANAGER', '01055556666', 'companymgr@example.com'),
      ('7e1d6210-b932-4c6f-b4d4-89c792408f01', FALSE, NOW(), 'delivery01', crypt('Test@1234', gen_salt('bf')), '배송01', 'U00000001', 'DELIVERY_MANAGER', '01010000001', 'delivery01@example.com'),
      ('bd9a178c-7c91-4984-9bc7-bf0d9c2e2701', FALSE, NOW(), 'delivery02', crypt('Test@1234', gen_salt('bf')), '배송02', 'U00000002', 'DELIVERY_MANAGER', '01010000002', 'delivery02@example.com'),
      ('c69fa8e5-7081-4c72-b8f6-1b57a2db8c8c', FALSE, NOW(), 'delivery03', crypt('Test@1234', gen_salt('bf')), '배송03', 'U00000003', 'DELIVERY_MANAGER', '01010000003', 'delivery03@example.com'),
      ('5a93be64-505a-49e4-9f24-0b7cf0f4cf3f', FALSE, NOW(), 'delivery04', crypt('Test@1234', gen_salt('bf')), '배송04', 'U00000004', 'DELIVERY_MANAGER', '01010000004', 'delivery04@example.com'),
      ('fd4e1f9c-bf89-4de3-8102-9f2ff3cf6d45', FALSE, NOW(), 'delivery05', crypt('Test@1234', gen_salt('bf')), '배송05', 'U00000005', 'DELIVERY_MANAGER', '01010000005', 'delivery05@example.com'),
      ('da4e49d5-2e3c-4f37-8c89-3e405edf8fe3', FALSE, NOW(), 'delivery06', crypt('Test@1234', gen_salt('bf')), '배송06', 'U00000006', 'DELIVERY_MANAGER', '01010000006', 'delivery06@example.com'),
      ('35cb0037-6d0c-4d2d-8085-9c46fc09b8ea', FALSE, NOW(), 'delivery07', crypt('Test@1234', gen_salt('bf')), '배송07', 'U00000007', 'DELIVERY_MANAGER', '01010000007', 'delivery07@example.com'),
      ('d6017163-12cd-403c-a159-8a11cf6b7a60', FALSE, NOW(), 'delivery08', crypt('Test@1234', gen_salt('bf')), '배송08', 'U00000008', 'DELIVERY_MANAGER', '01010000008', 'delivery08@example.com'),
      ('d8a3d251-3777-4f63-bf88-dbb5a6bb2720', FALSE, NOW(), 'delivery09', crypt('Test@1234', gen_salt('bf')), '배송09', 'U00000009', 'DELIVERY_MANAGER', '01010000009', 'delivery09@example.com'),
      ('53fe7cbe-f3a7-4b65-80cb-47a2de5d2eb4', FALSE, NOW(), 'delivery10', crypt('Test@1234', gen_salt('bf')), '배송10', 'U00000010', 'DELIVERY_MANAGER', '01010000010', 'delivery10@example.com'),
      ('fc707cab-5c8e-4963-80ad-40d53c87ff3b', FALSE, NOW(), 'delivery11', crypt('Test@1234', gen_salt('bf')), '배송11', 'U00000011', 'DELIVERY_MANAGER', '01010000011', 'delivery11@example.com'),
      ('e313e5f3-d7ed-4cb4-a48b-2d9d83db7d8a', FALSE, NOW(), 'delivery12', crypt('Test@1234', gen_salt('bf')), '배송12', 'U00000012', 'DELIVERY_MANAGER', '01010000012', 'delivery12@example.com'),
      ('3cf8a4ed-e2a1-4f36-89c4-44e50140f8a4', FALSE, NOW(), 'delivery13', crypt('Test@1234', gen_salt('bf')), '배송13', 'U00000013', 'DELIVERY_MANAGER', '01010000013', 'delivery13@example.com'),
      ('5efdf4f2-b370-43b3-90e2-1e80524ef0a3', FALSE, NOW(), 'delivery14', crypt('Test@1234', gen_salt('bf')), '배송14', 'U00000014', 'DELIVERY_MANAGER', '01010000014', 'delivery14@example.com'),
      ('e5f44902-5931-4b06-b4a2-9d02b0efabec', FALSE, NOW(), 'delivery15', crypt('Test@1234', gen_salt('bf')), '배송15', 'U00000015', 'DELIVERY_MANAGER', '01010000015', 'delivery15@example.com'),
      ('f5c2de95-d983-462a-897b-3a8b0e237ed0', FALSE, NOW(), 'delivery16', crypt('Test@1234', gen_salt('bf')), '배송16', 'U00000016', 'DELIVERY_MANAGER', '01010000016', 'delivery16@example.com'),
      ('93b4c3b7-d00f-44b0-8eb5-cd95e12e0c83', FALSE, NOW(), 'delivery17', crypt('Test@1234', gen_salt('bf')), '배송17', 'U00000017', 'DELIVERY_MANAGER', '01010000017', 'delivery17@example.com'),
      ('c3124730-b6b1-4b1c-bf58-bb28c4053a88', FALSE, NOW(), 'delivery18', crypt('Test@1234', gen_salt('bf')), '배송18', 'U00000018', 'DELIVERY_MANAGER', '01010000018', 'delivery18@example.com'),
      ('3e321cf8-c91f-4986-8f3a-25232a4a18cb', FALSE, NOW(), 'delivery19', crypt('Test@1234', gen_salt('bf')), '배송19', 'U00000019', 'DELIVERY_MANAGER', '01010000019', 'delivery19@example.com'),
      ('7791cd04-29ef-49f3-8f53-5fd4290d6f11', FALSE, NOW(), 'delivery20', crypt('Test@1234', gen_salt('bf')), '배송20', 'U00000020', 'DELIVERY_MANAGER', '01010000020', 'delivery20@example.com');
