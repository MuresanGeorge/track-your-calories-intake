--Consumption table
CREATE TABLE public.consumption
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    desired_daily_intake INTEGER NOT NULL,
    creation_date TIMESTAMP,

    CONSTRAINT consumption_pkey PRIMARY KEY (id)
);

--Meal table
CREATE TABLE public.meal
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR,
    consumption_id INTEGER,

    CONSTRAINT meal_pkey PRIMARY KEY (id),
    CONSTRAINT fk_meal_consumption FOREIGN KEY (consumption_id) REFERENCES public.consumption (id)
);

--Recipe table
CREATE TABLE public.recipe
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    calories INTEGER,
    consumption_id INTEGER,

    CONSTRAINT recipe_pkey PRIMARY KEY (id),
    CONSTRAINT fk_recipe_consumption FOREIGN KEY (consumption_id) REFERENCES public.consumption (id)
);

--Ingredient table
CREATE TABLE public.ingredient
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR NOT NULL,
    brand VARCHAR,
    carbohydrates INTEGER,
    proteins INTEGER,
    fats INTEGER,
    fibers INTEGER,

    CONSTRAINT ingredient_pkey PRIMARY KEY (id),
    CONSTRAINT ingredient_name_key UNIQUE (name)
);