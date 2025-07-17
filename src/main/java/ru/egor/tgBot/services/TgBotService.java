package ru.egor.tgBot.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.egor.tgBot.entity.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TgBotService {

    private  final TelegramBot bot = new TelegramBot("7977149638:AAFkUZQQd3zMwv4rvJyWUT3VkegP0drgogo");

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderProductService orderProductService;

    @Autowired
    private ClientOrderService clientOrderService;

    @Autowired
    private CategoryService categoryService;

    @PostConstruct
    private void start() {
        bot.setUpdatesListener(updates ->
        {
            updates.forEach(this::update);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void update(Update update){
        if (update.callbackQuery() != null && update.callbackQuery().data() != null) {
            String data = update.callbackQuery().data();
            if (data.startsWith("product:")) {
                Long productId = Long.parseLong(data.split(":")[1]);
                Long chatId = update.callbackQuery().message().chat().id();
                Client client = clientService.checkClient(chatId);
                ClientOrder clientOrder = clientOrderService.getClientOrder(client.getId());
                Product product = productService.getProductById(productId);
                orderProductService.addProductToOrder(clientOrder, product, 1L);
                bot.execute(new SendMessage(chatId,
                        product.getName() + " добавлен в заказ."));
                return;
            }
        }
        final Long chatId = update.message().chat().id();
        String secName = update.message().from().lastName();
        if(secName == null){
            secName = "";
        }
        Client client = clientService.checkClient(chatId);
        if(client == null){
            clientService.saveNewClient(chatId, update.message().from().firstName()+" "+ secName, "","");
            client = clientService.checkClient(chatId);
            clientOrderService.saveNewClientOrder(client,1,0.0);
        }
        ClientOrder clientOrder = clientOrderService.getClientOrder(client.getId());
        String messageText = update.message().text();
        Double total = 0.0;
        switch (messageText) {
            case "/start": {
                SendMessage message = new SendMessage(chatId, "Привет! Это бот для заказа еды. " +
                        "Выберите Категорию");
                bot.execute(message);
                List<KeyboardButton> categories = categoryService.getParentCategories()
                        .stream()
                        .map(category -> new KeyboardButton(category.getName()))
                        .collect(Collectors.toList());
                ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(categories.toArray(KeyboardButton[]::new));
                markup.resizeKeyboard(true);
                markup.addRow(new KeyboardButton("Оформить заказ"));
                bot.execute(new SendMessage(update.message().chat().id(),
                        "Выберите Категорию").replyMarkup(markup));
                break;
            }
            case "В основное меню": {
                List<KeyboardButton> mainCategories = categoryService.getParentCategories()
                        .stream()
                        .map(category -> new KeyboardButton(category.getName()))
                        .collect(Collectors.toList());

                ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(mainCategories.toArray(new KeyboardButton[0]));
                markup.resizeKeyboard(true);
                markup.addRow(new KeyboardButton("Оформить заказ"));

                bot.execute(new SendMessage(chatId, "Вы вернулись в главное меню.").replyMarkup(markup));
                break;
            }
            case "Оформить заказ": {
                List<OrderProduct> orderProduct = orderProductService.findOrderProductsByClientOrderId(clientOrder.getId());
                StringBuilder sb = new StringBuilder();
                sb.append("Ваш заказ:\n");
                for (OrderProduct op : orderProduct) {
                    Product product = op.getProduct();
                    total += product.getPrice() * op.getCountProduct();
                    sb.append(op.getProduct().getName() + " - " + op.getCountProduct() + "шт. - " + product.getPrice() + "руб."  + "\n");
                }
                ReplyKeyboardMarkup backMarkup = new ReplyKeyboardMarkup(
                        new KeyboardButton[] {
                                new KeyboardButton("Подтвердить заказ"),
                                new KeyboardButton("В основное меню")
                        });
                backMarkup.resizeKeyboard(true);
                bot.execute(new SendMessage(chatId, sb.toString() + "\nИтого: " + total + "руб.").replyMarkup(backMarkup));
                break;
            }
            case "Подтвердить заказ":{
                List<OrderProduct> orderProduct = orderProductService.findOrderProductsByClientOrderId(clientOrder.getId());
                for (OrderProduct op : orderProduct) {
                    Product product = op.getProduct();
                    total += product.getPrice() * op.getCountProduct();
                }
                clientOrderService.updateClientOrder(clientOrder, total);
                ReplyKeyboardMarkup backMarkup = new ReplyKeyboardMarkup(new KeyboardButton("В основное меню"));
                bot.execute(new SendMessage(chatId, "Заказ на сумму " + total + "руб. оформлен.").replyMarkup(backMarkup));
                clientOrderService.saveNewClientOrder(client,1,0.0);
                break;
            }
            default: {
                Category category = categoryService.getCategoryByName(messageText);
                if (category != null) {
                    List<Category> childCategories = categoryService.getChildCategories(category.getId());
                    if (childCategories != null && !childCategories.isEmpty()) {
                        List<KeyboardButton> buttons = childCategories.stream()
                                .map(cat -> new KeyboardButton(cat.getName()))
                                .collect(Collectors.toList());
                        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(buttons.toArray(new KeyboardButton[0]));
                        markup.resizeKeyboard(true);
                        markup.addRow(new KeyboardButton("Оформить заказ"));
                        markup.addRow(new KeyboardButton("В основное меню"));
                        bot.execute(new SendMessage(chatId, "Выберите подкатегорию:").replyMarkup(markup));
                    } else {
                        List<Product> products = productService.getProductsByCategoryId(category.getId());
                        if (!products.isEmpty()) {
                            InlineKeyboardMarkup productMarkup = new InlineKeyboardMarkup();
                            for (Product product : products) {
                                InlineKeyboardButton button = new InlineKeyboardButton(
                                        product.getName() + " - " + product.getPrice() + "₽"
                                ).callbackData("product:" + product.getId());
                                productMarkup.addRow(button);
                            }
                            bot.execute(new SendMessage(chatId, "Выберите товар:").replyMarkup(productMarkup));
                            ReplyKeyboardMarkup backMarkup = new ReplyKeyboardMarkup(
                                    new KeyboardButton[] {
                                            new KeyboardButton("Оформить заказ"),
                                            new KeyboardButton("В основное меню")
                                    });
                            backMarkup.resizeKeyboard(true);
                            bot.execute(new SendMessage(chatId, "Вы также можете оформить заказ или вернуться в меню.")
                                    .replyMarkup(backMarkup));
                        } else {
                            bot.execute(new SendMessage(chatId, "Товары в этой категории отсутствуют."));
                        }
                    }
                }
                break;
            }
        }
    }
}
