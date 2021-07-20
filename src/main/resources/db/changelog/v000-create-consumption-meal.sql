CREATE TABLE public.consumption_meal
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    consumption_id BIGINT NOT NULL,
    meal_id BIGINT NOT NULL,

    CONSTRAINT fk_consumption_consumption_meal FOREIGN KEY (consumption_id) REFERENCES public.consumption (id),
    CONSTRAINT fk_meal_consumption_meal FOREIGN KEY (meal_id) REFERENCES public.meal (id)
);