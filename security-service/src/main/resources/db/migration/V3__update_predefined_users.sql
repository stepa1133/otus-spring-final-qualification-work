INSERT INTO users (username, password) VALUES
    ('chief', '$2a$10$ctFyUPzEnM9lxPlsOtm6I.R.Y.d40N1O00rUUMUvjomdU.AlxTPBC');

UPDATE users_roles
SET role_Id = 3
WHERE user_id = 1;

UPDATE users_roles
SET role_Id = 1
WHERE user_id = 2;