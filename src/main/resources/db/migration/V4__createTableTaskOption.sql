CREATE TABLE Task_Option (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text VARCHAR(80) NOT NULL,
    is_correct BOOLEAN NOT NULL,
    option_order INT NOT NULL,
    task_id BIGINT NOT NULL,
    FOREIGN KEY (task_id) REFERENCES Task(id) ON DELETE CASCADE,
    INDEX idx_task_option_task_id (task_id),
    INDEX idx_task_option_order (task_id, option_order)
);