INSERT INTO delivery_manager.p_delivery_manager (
    dm_id, dm_user_id, dm_hub_id, dm_slack_id, dm_type, dm_turn, is_deleted, created_at
) VALUES
-- HUB 타입 (허브 미지정)
('11a0a0a0-1111-4111-8111-111111111111', '7e1d6210-b932-4c6f-b4d4-89c792408f01', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB1', 'HUB', 0, false, NOW()),
('22b0b0b0-2222-4222-8222-222222222222', 'bd9a178c-7c91-4984-9bc7-bf0d9c2e2701', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB2', 'HUB', 1, false, NOW()),
('33c0c0c0-3333-4333-8333-333333333333', 'c69fa8e5-7081-4c72-b8f6-1b57a2db8c8c', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB3', 'HUB', 2, false, NOW()),
('44d0d0d0-4444-4444-8444-444444444444', '5a93be64-505a-49e4-9f24-0b7cf0f4cf3f', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB4', 'HUB', 3, false, NOW()),
('55e0e0e0-5555-4555-8555-555555555555', 'fd4e1f9c-bf89-4de3-8102-9f2ff3cf6d45', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB5', 'HUB', 4, false, NOW()),
('66f0f0f0-6666-4666-8666-666666666666', 'da4e49d5-2e3c-4f37-8c89-3e405edf8fe3', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB6', 'HUB', 5, false, NOW()),
('77a1a1a1-7777-4777-8777-777777777777', '35cb0037-6d0c-4d2d-8085-9c46fc09b8ea', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB7', 'HUB', 6, false, NOW()),
('88b2b2b2-8888-4888-8888-888888888888', 'd6017163-12cd-403c-a159-8a11cf6b7a60', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB8', 'HUB', 7, false, NOW()),
('99c3c3c3-9999-4999-8999-999999999999', 'd8a3d251-3777-4f63-bf88-dbb5a6bb2720', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB9', 'HUB', 8, false, NOW()),
('aa040404-aaaa-4aaa-8aaa-aaaaaaaaaaaa', '53fe7cbe-f3a7-4b65-80cb-47a2de5d2eb4', NULL, 'https://hooks.slack.com/services/AAA/BBB/HUB10', 'HUB', 9, false, NOW()),

-- COMPANY 타입 (서울특별시)
('bb111111-bbbb-4111-8bbb-bbbbbbbbbbbb', 'fc707cab-5c8e-4963-80ad-40d53c87ff3b', 'fd8c9c66-b391-4609-ac73-4faa8f6306f2', 'https://hooks.slack.com/services/AAA/BBB/COMPANY01', 'COMPANY', 0, false, NOW()),
('cc222222-cccc-4222-8ccc-cccccccccccc', 'e313e5f3-d7ed-4cb4-a48b-2d9d83db7d8a', 'fd8c9c66-b391-4609-ac73-4faa8f6306f2', 'https://hooks.slack.com/services/AAA/BBB/COMPANY02', 'COMPANY', 1, false, NOW()),
('dd333333-dddd-4333-8ddd-dddddddddddd', '3cf8a4ed-e2a1-4f36-89c4-44e50140f8a4', 'fd8c9c66-b391-4609-ac73-4faa8f6306f2', 'https://hooks.slack.com/services/AAA/BBB/COMPANY03', 'COMPANY', 2, false, NOW()),
('ee444444-eeee-4444-8eee-eeeeeeeeeeee', '5efdf4f2-b370-43b3-90e2-1e80524ef0a3', 'fd8c9c66-b391-4609-ac73-4faa8f6306f2', 'https://hooks.slack.com/services/AAA/BBB/COMPANY04', 'COMPANY', 3, false, NOW()),
('ff555555-ffff-4555-8fff-ffffffffffff', 'e5f44902-5931-4b06-b4a2-9d02b0efabec', 'fd8c9c66-b391-4609-ac73-4faa8f6306f2', 'https://hooks.slack.com/services/AAA/BBB/COMPANY05', 'COMPANY', 4, false, NOW()),

-- COMPANY 타입 (고양시)
('aa666666-aaaa-4666-8aaa-aaaaaaaaaaaa', 'f5c2de95-d983-462a-897b-3a8b0e237ed0', '2c4dd270-708a-4619-a4cd-0f41365335cb', 'https://hooks.slack.com/services/AAA/BBB/COMPANY06', 'COMPANY', 0, false, NOW()),
('bb777777-bbbb-4777-8bbb-bbbbbbbbbbbb', '93b4c3b7-d00f-44b0-8eb5-cd95e12e0c83', '2c4dd270-708a-4619-a4cd-0f41365335cb', 'https://hooks.slack.com/services/AAA/BBB/COMPANY07', 'COMPANY', 1, false, NOW()),
('cc888888-cccc-4888-8ccc-cccccccccccc', 'c3124730-b6b1-4b1c-bf58-bb28c4053a88', '2c4dd270-708a-4619-a4cd-0f41365335cb', 'https://hooks.slack.com/services/AAA/BBB/COMPANY08', 'COMPANY', 2, false, NOW()),
('dd999999-dddd-4999-8ddd-dddddddddddd', '3e321cf8-c91f-4986-8f3a-25232a4a18cb', '2c4dd270-708a-4619-a4cd-0f41365335cb', 'https://hooks.slack.com/services/AAA/BBB/COMPANY09', 'COMPANY', 3, false, NOW()),
('ee000000-eeee-4000-8eee-eeeeeeeeeeee', '7791cd04-29ef-49f3-8f53-5fd4290d6f11', '2c4dd270-708a-4619-a4cd-0f41365335cb', 'https://hooks.slack.com/services/AAA/BBB/COMPANY10', 'COMPANY', 4, false, NOW());
