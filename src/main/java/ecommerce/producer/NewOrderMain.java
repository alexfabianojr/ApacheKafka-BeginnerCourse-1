package ecommerce.producer;

import ecommerce.entidade.Email;
import ecommerce.entidade.Order;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/** PRODUCER */
public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        try (var orderDispatcher = new KafkaDispatcher<Order>();
             var emailDispatcher = new KafkaDispatcher<Email>()) {

            var email = new Email("Novo pedido", "Um novo pedido foi realizado com sucesso! ");

           for (int i = 0; i < 15; i++) {

                var userId = UUID.randomUUID().toString(); //Simula o ID de um usuário
                var orderId = UUID.randomUUID().toString();
                var amount = BigDecimal.valueOf(Math.random() * 5000.0 + 1.0);

                var order = new Order(userId, orderId, amount);

                orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);

                emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, email);

                Thread.sleep(5000);
            }
        }
    }
}
