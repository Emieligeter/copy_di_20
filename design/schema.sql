CREATE TABLE project.simulations (
  simID INT PRIMARY KEY,
  name TEXT,
  date DATE,
  description TEXT,
  researcher TEXT,
  net JSON NOT NULL,
  routes JSON NOT NULL,
  config JSON NOT NULL
);

CREATE TABLE project.states (
  simID INT NOT NULL,
  timestamp FLOAT,
  state JSON NOT NULL,
  PRIMARY KEY (simID, timestamp),
  CONSTRAINT sim_fkey FOREIGN KEY (simID) REFERENCES project.simulations
);

CREATE TABLE project.tags (
  tagID INT PRIMARY KEY,
  value TEXT NOT NULL
);

CREATE TABLE project.simulation_tags (
  tagID INT NOT NULL,
  simID INT NOT NULL,
  PRIMARY KEY (tagID, simID),
  CONSTRAINT tag_fkey FOREIGN KEY (tagID) REFERENCES project.tags,
  CONSTRAINT sim_fkey FOREIGN KEY (simID) REFERENCES project.simulations
);
