  --liquibase formatted sql
  --changeset bannikov:1

CREATE INDEX users_id_index ON student (id)


CREATE INDEX faculty_id_index ON faculty (name, color)