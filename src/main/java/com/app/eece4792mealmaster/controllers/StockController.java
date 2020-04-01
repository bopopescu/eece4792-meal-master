package com.app.eece4792mealmaster.controllers;

import static com.app.eece4792mealmaster.constants.Routes.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import com.app.eece4792mealmaster.models.FoodStock;
import com.app.eece4792mealmaster.models.GenericFood;
import com.app.eece4792mealmaster.models.StockItem;
import com.app.eece4792mealmaster.services.GenericFoodService;
import com.app.eece4792mealmaster.services.StockService;
import com.app.eece4792mealmaster.utils.ApiResponse;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;

import com.app.eece4792mealmaster.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = {"*", "http://localhost:3000"}, allowCredentials = "true")
public class StockController {

	@Autowired
	private StockService stockService;

	@Autowired
	private GenericFoodService genericFoodService;
	
	@GetMapping(STOCK_API + VAR_STOCK_ID)
	public ApiResponse getStockById(HttpSession session, @PathVariable(STOCK_ID) Long stockId) {
		Long userId = Utils.getLoggedInUser(session);
		if (userId == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		return new ApiResponse(stockService.getFoodStockById(stockId));
	}

	@GetMapping(STOCK_API + FOOD + VAR_FOOD_ID)
	public ApiResponse getStockByFood(HttpSession session, @PathVariable(FOOD_ID) Long foodId) {
			Long userId = Utils.getLoggedInUser(session);
			if (userId == null) {
					throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
			}
			return new ApiResponse(stockService.getFoodStockByFood(userId, foodId));
	}

	@GetMapping(STOCK_API)
	public ApiResponse getUserStock(HttpSession session) {
		Long userId = Utils.getLoggedInUser(session);
		if (userId == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		return new ApiResponse(stockService.getStockByUser(userId));
	}

	@GetMapping(STOCK_ITEM_API + VAR_STOCK_ITEM_ID)
	public ApiResponse getStockItemById(HttpSession session, @PathVariable(STOCK_ITEM_ID) Long stockItemId) {
		Long userId = Utils.getLoggedInUser(session);
		if (userId == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		return new ApiResponse(stockService.getStockItemById(stockItemId));
	}

	@PostMapping(AZURE)
	public ApiResponse receiptStock(HttpSession session, @RequestBody String imgUrl) throws IOException {
		Long userId = Utils.getLoggedInUser(session);

		if (userId == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		if (imgUrl == null) {
			imgUrl = "https://raw.githubusercontent.com/Team-W4/eece4792-meal-master/text-recognition/src/text-recognition/receipt-pics/tj1.jpg";
		}
		imgUrl = "https://raw.githubusercontent.com/Team-W4/eece4792-meal-master/text-recognition/src/text-recognition/receipt-pics/tj1.jpg";

		String text = new String();
		if(System.getProperty("os.name").toLowerCase().contains("win")) {
			text = "eece4792-meal-master/src/text-recognition/dist/parse-receipt/parse-receipt.exe --image_path " + imgUrl;
		} else {
			text = "parse-receipt_ub/parse-receipt_ub --image_path " + imgUrl;
		}

		Process p = Runtime.getRuntime().exec(text);

		// =======================================================================================================================
		// Process proc = Runtime.getRuntime().exec(text);		
		// BufferedReader stdInput = new BufferedReader(new 
		// InputStreamReader(proc.getInputStream()));

		// BufferedReader stdError = new BufferedReader(new 
		// 	InputStreamReader(proc.getErrorStream()));

		// Read the output from the command
		// System.out.println("Here is the standard output of the command:\n");
		// String s = null;
		// while ((s = stdInput.readLine()) != null) {
		// 	System.out.println(s);
		// }

		// // Read any errors from the attempted command
		// System.out.println("Here is the standard error of the command (if any):\n");
		// while ((s = stdError.readLine()) != null) {
		// 	System.out.println(s);
		// }
		// =======================================================================================================================

		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String pyString = null; 
		String id_list = new String();
		while ((pyString = input.readLine()) != null) {
			id_list = pyString;
		}

		String[] items = id_list.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

		long[] results = new long[items.length];		
		for (int i = 0; i < items.length; i++) {
			try {
				results[i] = Long.parseLong(items[i]);
			} catch (NumberFormatException nfe) {
			};
		}

		Set<GenericFood> scannedFoods = new HashSet<>();
		for (int j = 0; j < results.length; j++) {
			System.out.println(results[j]);
			GenericFood scannedFood = genericFoodService.getGenericFoodById(results[j]);
			scannedFoods.add(scannedFood);
		}

		return new ApiResponse(scannedFoods);
	}

	@PostMapping(STOCK_ITEM_API + FOOD + VAR_FOOD_ID)
	public ApiResponse addToStock(HttpSession session, @PathVariable(FOOD_ID) Long foodId, @RequestBody StockItem payload) {
		Long userId = Utils.getLoggedInUser(session);
		if (userId == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		return new ApiResponse(stockService.addToStock(foodId, payload, userId));
	}

	@PutMapping(STOCK_ITEM_API)
	public ApiResponse updateStockItem(HttpSession session, @RequestBody StockItem payload) {
		Long userId = Utils.getLoggedInUser(session);
		if (userId == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		return new ApiResponse(stockService.updateStockItem(payload));
	}

	@DeleteMapping(STOCK_ITEM_API + VAR_STOCK_ITEM_ID)
	public ApiResponse deleteStock(HttpSession session, @PathVariable(STOCK_ITEM_ID) Long stockItemId) {
		Long userId = Utils.getLoggedInUser(session);
		if (userId == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		if (!stockService.deleteStockItem(stockItemId)) {
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ApiResponse(String.format("StockItem %d deleted", stockItemId));
	}

	@DeleteMapping(STOCK_API + VAR_STOCK_ID)
	public ApiResponse deleteFoodStock(HttpSession session, @PathVariable(STOCK_ID) Long foodStockId) {
		Long userId = Utils.getLoggedInUser(session);
		if (userId == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		FoodStock foodStock = stockService.getFoodStockById(foodStockId);
		Set<StockItem> stockItems = foodStock.getStockItems();
		Set<Long> stockItemIds = stockItems.stream().map(StockItem::getId).collect(Collectors.toSet());
		try {
			stockService.deleteStockItems(stockItemIds);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if (!stockService.deleteFoodStock(foodStockId)) {
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ApiResponse(String.format("Food stock %d deleted", foodStockId));
	}
}
