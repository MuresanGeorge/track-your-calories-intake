CREATE TABLE public.ingredient_stock
(
    ingredient_id BIGINT,
    recipe_id BIGINT,
    quantity INTEGER NOT NULL,
    PRIMARY KEY (ingredient_id, recipe_id),

    CONSTRAINT fk_ingredient_stock_ingredient FOREIGN KEY (ingredient_id) REFERENCES public.ingredient (id),
    CONSTRAINT fk_ingredient_stock_recipe FOREIGN KEY (recipe_id) REFERENCES public.recipe (id)

)