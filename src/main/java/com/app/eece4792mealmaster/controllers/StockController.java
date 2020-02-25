package com.app.eece4792mealmaster.controllers;

import static com.app.eece4792mealmaster.controllers.Routes.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import com.app.eece4792mealmaster.models.StockItem;
import com.app.eece4792mealmaster.services.GenericFoodService;
import com.app.eece4792mealmaster.services.StockService;
import com.app.eece4792mealmaster.utils.ApiResponse;

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
		// Takes url in body, returns food id's in body as list ex: [21, 38, 2113, 321]
		Long userId = Utils.getLoggedInUser(session);
		System.out.println(imgUrl);
		if (userId == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		if (imgUrl == null) {
			imgUrl = "https://raw.githubusercontent.com/Team-W4/eece4792-meal-master/text-recognition/src/text-recognition/receipt-pics/tj1.jpg";
		}
		imgUrl = "https://raw.githubusercontent.com/Team-W4/eece4792-meal-master/text-recognition/src/text-recognition/receipt-pics/tj1.jpg";

		// https://raw.githubusercontent.com/Team-W4/eece4792-meal-master/text-recognition/src/text-recognition/receipt-pics/tj1.jpg
		String text = new String();
		if(System.getProperty("os.name").toLowerCase().contains("win"))
			text = "src/text-recognition/dist/parse-receipt/parse-receipt.exe --image_path "+imgUrl;
		else
			text = "src/text-recognition/dist/parse-receipt_ub/parse-receipt --image_path "+imgUrl;
		final String dir = System.getProperty("user.dir");
        System.out.println("current dir = " + dir);
		Process p = Runtime.getRuntime().exec(text);
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String pyString = input.readLine(); 
		String id_list = new String();
		while ((pyString = input.readLine()) != null) {
			// print the line.
			System.out.println(pyString);
			id_list = pyString;
		}
		System.out.println(id_list);

		return new ApiResponse(id_list);
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
}
