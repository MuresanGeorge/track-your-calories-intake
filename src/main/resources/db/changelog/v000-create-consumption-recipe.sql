CREATE TABLE public.consumption_recipe
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    consumption_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,

    CONSTRAINT fk_consumption_consumption_meal FOREIGN KEY (consumption_id) REFERENCES public.consumption (id),
    CONSTRAINT fk_recipe_consumption_meal FOREIGN KEY (recipe_id) REFERENCES public.recipe (id)
);