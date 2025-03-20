CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

INSERT INTO "user".p_user (
    user_id, is_deleted, created_at, user_name, user_password, user_nickname, user_slack_id, user_role, user_phone, user_email
) VALUES
      (uuid_generate_v4(), FALSE, NOW(), 'master', crypt('Test@1234', gen_salt('bf')), '마스터', 'U12345678', 'MASTER', '01011112222', 'master@example.com'),
      (uuid_generate_v4(), FALSE, NOW(), 'hubmng', crypt('Test@1234', gen_salt('bf')), '허브매니저', 'U87654321', 'HUB_MANAGER', '01022223333', 'hubmanager@example.com'),
      (uuid_generate_v4(), FALSE, NOW(), 'deliverymng', crypt('Test@1234', gen_salt('bf')), '배송매니저', 'U56781234', 'DELIVERY_MANAGER', '01033334444', 'deliverymgr@example.com'),
      (uuid_generate_v4(), FALSE, NOW(), 'companymng', crypt('Test@1234', gen_salt('bf')), '회사매니저', 'U98765432', 'COMPANY_MANAGER', '01055556666', 'companymgr@example.com');
