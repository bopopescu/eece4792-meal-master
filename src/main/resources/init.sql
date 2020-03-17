USE mealmaster;

delimiter //

DROP FUNCTION IF EXISTS recipeCanBeMade //
CREATE FUNCTION recipeCanBeMade
(
	user INT,
    recipe INT
)
RETURNS DOUBLE DETERMINISTIC
BEGIN
	DECLARE countPossible DOUBLE;

	SELECT MIN(IFNULL(totalQuantity, 0) / servings) INTO countPossible
	FROM (
		SELECT ingredient_id, servings
		FROM recipes r JOIN recipe_ingredients ri ON (r.id = ri.recipe_id)
		WHERE r.id = recipe
	) a
	LEFT JOIN
	(
		SELECT gf.id as ingredient_id, SUM(quantity) as totalQuantity
		FROM generic_foods gf LEFT JOIN food_stock fs ON (gf.id = fs.food_id)
			JOIN stock_item si ON (fs.id = si.food_stock_id)
		WHERE user_id = user GROUP BY fs.id
	) b
	USING (ingredient_id);
    
    RETURN countPossible;
END //

delimiter ;
