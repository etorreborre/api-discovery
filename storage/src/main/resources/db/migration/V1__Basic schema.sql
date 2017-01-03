-- base entity: application
CREATE TABLE api (
-- the official application ID, like 'kio' or 'pierone'
  application_id   TEXT NOT NULL,
-- status of current crawling
  status           TEXT NOT NULL,

-- type of API definition
  type             TEXT,
-- the APIs name
  name             TEXT,
-- the APIs version
  version          TEXT,
-- URL to the definition
  url              TEXT,
-- URL to a human browseable API interface
  ui               TEXT,
-- definition of the API
  definition       TEXT,

  PRIMARY KEY (application_id)
);
