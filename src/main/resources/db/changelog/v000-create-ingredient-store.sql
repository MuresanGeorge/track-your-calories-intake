create TABLE public.ingredient_store (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    amount INTEGER NOT NULL,
    ingredient_id BIGINT,
    meal_id BIGINT,

    CONSTRAINT ingredient_store_pkey PRIMARY KEY (id),
    CONSTRAINT fk_ingredient_store_ingredient FOREIGN KEY (ingredient_id) REFERENCES public.ingredient (id),
    CONSTRAINT fk_ingredient_store_meal FOREIGN KEY (meal_id) REFERENCES public.meal (id)
)