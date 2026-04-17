CREATE TABLE goal_achievement_records (
    id UUID PRIMARY KEY,
    goal_id UUID NOT NULL REFERENCES goals(id),
    achieved_amount DECIMAL(19, 4) NOT NULL,
    completion_date TIMESTAMP NOT NULL,
    time_to_completion_days INT NOT NULL,
    lessons_learned TEXT
);

ALTER TABLE goals ADD COLUMN paused BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE automated_transfers ADD COLUMN paused BOOLEAN NOT NULL DEFAULT FALSE;
