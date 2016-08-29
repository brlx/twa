ALTER TABLE users
ADD COLUMN last_login timestamp without time zone;

ALTER TABLE users
ADD COLUMN cookie text;

COMMENT ON COLUMN users.cookie IS 'The clients will pass this cookie to the REST services when logged in.';
