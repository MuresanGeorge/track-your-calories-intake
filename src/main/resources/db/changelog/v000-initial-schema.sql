--Consumption table
create TABLE public.consumption
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    desired_daily_intake INTEGER NOT NULL,
    creation_date TIMESTAMP,
    carbohydrates INTEGER NOT NULL,
    proteins INTEGER NOT NULL,
    fats INTEGER NOT NULL,

    CONSTRAINT consumption_pkey PRIMARY KEY (id)
);

--Meal table
create TABLE public.meal
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR,

    CONSTRAINT meal_pkey PRIMARY KEY (id)
);

--Recipe table
create TABLE public.recipe
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    calories INTEGER,

    CONSTRAINT recipe_pkey PRIMARY KEY (id)
);

--Ingredient table
create TABLE public.ingredient
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR NOT NULL,
    brand VARCHAR,
    carbohydrates INTEGER,
    proteins INTEGER,
    fats INTEGER,
    fibers INTEGER,

    CONSTRAINT ingredient_pkey PRIMARY KEY (id)
);