ALTER TABLE otp_verifications ADD COLUMN attempts INT NOT NULL DEFAULT 0;
DELETE FROM otp_verifications WHERE expires_at < CURRENT_TIMESTAMP OR used_at IS NOT NULL;
