CREATE TABLE IF NOT EXISTS "family" (
    "id" bigserial PRIMARY KEY,
    "code" varchar(255) NOT NULL UNIQUE,
    "created_at" timestamp with time zone NOT NULL
);

CREATE TABLE IF NOT EXISTS "users" (
    "id" bigserial PRIMARY KEY,
    "email" varchar(100) NOT NULL,
    "name" varchar(100) NOT NULL,
    "family_id" bigint NOT NULL,
    "password_hash" varchar(255) NOT NULL,
    "created_at" timestamp with time zone NOT NULL,
    CONSTRAINT "users_fk0" FOREIGN KEY ("family_id") REFERENCES "family"("id")
);

CREATE TABLE IF NOT EXISTS "task_categories" (
    "id" bigserial PRIMARY KEY,
    "name" varchar(100) NOT NULL,
    "created_at" timestamp with time zone NOT NULL
);

CREATE TABLE IF NOT EXISTS "task_types" (
    "id" bigserial PRIMARY KEY,
    "category_id" bigint NOT NULL,
    "name" varchar(100) NOT NULL,
    "point" bigint NOT NULL,
    "created_at" timestamp with time zone NOT NULL,
    CONSTRAINT "task_types_fk1" FOREIGN KEY ("category_id") REFERENCES "task_categories"("id")
);

CREATE TABLE IF NOT EXISTS "tasks" (
    "id" bigserial PRIMARY KEY,
    "user_id" bigint NOT NULL,
    "task_type_id" bigint NOT NULL,
    "created_at" timestamp with time zone NOT NULL,
    CONSTRAINT "tasks_fk1" FOREIGN KEY ("user_id") REFERENCES "users"("id"),
    CONSTRAINT "tasks_fk2" FOREIGN KEY ("task_type_id") REFERENCES "task_types"("id")
);

CREATE TABLE IF NOT EXISTS "sessions" (
    "id" bigserial PRIMARY KEY,
    "user_id" bigint NOT NULL,
    "token" varchar(255) NOT NULL UNIQUE,
    "expires_at" timestamp with time zone NOT NULL,
    "created_at" timestamp with time zone NOT NULL,
    CONSTRAINT "sessions_fk1" FOREIGN KEY ("user_id") REFERENCES "users"("id")
);
