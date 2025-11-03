CREATE TABLE benchmark
(
    id INT AUTO_INCREMENT,
    user_id INT,
    overall_score DOUBLE NOT NULL,
    created_at TIMESTAMP,
    description VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE benchmark_result (
    id INT AUTO_INCREMENT PRIMARY KEY,
    benchmark_id INT NOT NULL,
    hardware_id INT,
    hardware_type ENUM('CPU', 'GPU', 'RAM') NOT NULL,
    metric_name VARCHAR(50) NOT NULL,
    metric_value DOUBLE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (benchmark_id) REFERENCES benchmark(id) ON DELETE CASCADE
);

CREATE TABLE system_configuration
(
    id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    cpu_id INT,
    gpu_id INT,
    ram_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (cpu_id) REFERENCES cpu(id),
    FOREIGN KEY (gpu_id) REFERENCES gpu(id),
    FOREIGN KEY (ram_id) REFERENCES ram(id)
);

ALTER TABLE benchmark ADD COLUMN configuration_id INT,
                      ADD FOREIGN KEY (configuration_id) REFERENCES system_configuration(id);

DELIMITER $$

CREATE TRIGGER set_created_at_benchmark_result
    BEFORE INSERT ON benchmark_result
    FOR EACH ROW
BEGIN
    DECLARE benchmark_created_at TIMESTAMP;

    -- Retrieve the created_at timestamp from the benchmark table
    SELECT created_at INTO benchmark_created_at
    FROM benchmark
    WHERE id = NEW.benchmark_id;

    -- Set the created_at value in benchmark_result to match
    SET NEW.created_at = benchmark_created_at;
END;
$$

DELIMITER ;