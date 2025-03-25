INSERT INTO delivery_manager.p_delivery_manager (dm_id, dm_user_id, dm_hub_id, dm_slack_id, dm_type, dm_turn,
                                                 is_deleted, created_at)
VALUES
-- HUB 타입 (허브 미지정)
('40000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000007', NULL,
 'https://hooks.slack.com/services/AAA/BBB/HUB1', 'HUB', 0, false, NOW()),
('40000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000008', NULL,
 'https://hooks.slack.com/services/AAA/BBB/HUB2', 'HUB', 1, false, NOW()),
('40000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000009', NULL,
 'https://hooks.slack.com/services/AAA/BBB/HUB3', 'HUB', 2, false, NOW()),
-- ('40000000-0000-0000-0000-000000000004', '5a93be64-505a-49e4-9f24-0b7cf0f4cf3f', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB4', 'HUB', 3, false, NOW()),
-- ('40000000-0000-0000-0000-000000000005', 'fd4e1f9c-bf89-4de3-8102-9f2ff3cf6d45', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB5', 'HUB', 4, false, NOW()),
-- ('40000000-0000-0000-0000-000000000006', 'da4e49d5-2e3c-4f37-8c89-3e405edf8fe3', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB6', 'HUB', 5, false, NOW()),
-- ('40000000-0000-0000-0000-000000000007', '35cb0037-6d0c-4d2d-8085-9c46fc09b8ea', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB7', 'HUB', 6, false, NOW()),
-- ('40000000-0000-0000-0000-000000000008', 'd6017163-12cd-403c-a159-8a11cf6b7a60', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB8', 'HUB', 7, false, NOW()),
-- ('40000000-0000-0000-0000-000000000009', 'd8a3d251-3777-4f63-bf88-dbb5a6bb2720', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB9', 'HUB', 8, false, NOW()),
-- ('40000000-0000-0000-0000-000000000010', '53fe7cbe-f3a7-4b65-80cb-47a2de5d2eb4', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB10', 'HUB', 9, false, NOW()),

-- COMPANY 타입 (서울특별시)
('40000000-0000-0000-0000-000000000011', '00000000-0000-0000-0000-000000000010', '10000000-0000-0000-0000-000000000002',
 'https://hooks.slack.com/services/AAA/BBB/COMPANY01', 'COMPANY', 0, false, NOW());
-- ('40000000-0000-0000-0000-000000000012', 'e313e5f3-d7ed-4cb4-a48b-2d9d83db7d8a', 'fd8c9c66-b391-4609-ac73-4faa8f6306f2', 'https://hooks.slack.com/services/AAA/BBB/COMPANY02', 'COMPANY', 1, false, NOW()),
-- ('40000000-0000-0000-0000-000000000013', '3cf8a4ed-e2a1-4f36-89c4-44e50140f8a4', 'fd8c9c66-b391-4609-ac73-4faa8f6306f2', 'https://hooks.slack.com/services/AAA/BBB/COMPANY03', 'COMPANY', 2, false, NOW()),
-- ('40000000-0000-0000-0000-000000000014', '5efdf4f2-b370-43b3-90e2-1e80524ef0a3', 'fd8c9c66-b391-4609-ac73-4faa8f6306f2', 'https://hooks.slack.com/services/AAA/BBB/COMPANY04', 'COMPANY', 3, false, NOW()),
-- ('40000000-0000-0000-0000-000000000015', 'e5f44902-5931-4b06-b4a2-9d02b0efabec', 'fd8c9c66-b391-4609-ac73-4faa8f6306f2', 'https://hooks.slack.com/services/AAA/BBB/COMPANY05', 'COMPANY', 4, false, NOW()),

-- COMPANY 타입 (고양시)
-- ('40000000-0000-0000-0000-000000000016', 'f5c2de95-d983-462a-897b-3a8b0e237ed0', '2c4dd270-708a-4619-a4cd-0f41365335cb', 'https://hooks.slack.com/services/AAA/BBB/COMPANY06', 'COMPANY', 0, false, NOW()),
-- ('40000000-0000-0000-0000-000000000017', '93b4c3b7-d00f-44b0-8eb5-cd95e12e0c83', '2c4dd270-708a-4619-a4cd-0f41365335cb', 'https://hooks.slack.com/services/AAA/BBB/COMPANY07', 'COMPANY', 1, false, NOW()),
-- ('40000000-0000-0000-0000-000000000018', 'c3124730-b6b1-4b1c-bf58-bb28c4053a88', '2c4dd270-708a-4619-a4cd-0f41365335cb', 'https://hooks.slack.com/services/AAA/BBB/COMPANY08', 'COMPANY', 2, false, NOW()),
-- ('40000000-0000-0000-0000-000000000019', '3e321cf8-c91f-4986-8f3a-25232a4a18cb', '2c4dd270-708a-4619-a4cd-0f41365335cb', 'https://hooks.slack.com/services/AAA/BBB/COMPANY09', 'COMPANY', 3, false, NOW()),
-- ('40000000-0000-0000-0000-000000000020', '7791cd04-29ef-49f3-8f53-5fd4290d6f11', '2c4dd270-708a-4619-a4cd-0f41365335cb', 'https://hooks.slack.com/services/AAA/BBB/COMPANY10', 'COMPANY', 4, false, NOW());