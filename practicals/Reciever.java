package org.example.practicals;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.MessageConsumer;

public
class Reciever extends AbstractVerticle
{
    @Override
    public void start()
    {
//       vertx.executeBlocking(()->{
//           Promise<Integer> promise = Promise.promise();
//          for(int i = 0; i < 10000; i++){
//              for(int j = 0; j < 10000; j++){
//                  for(int k = 0; k < 10000; k++){
//
//                  }
//              }
//          }
//
//          promise.complete(1);
//          return promise.future();
//       });

        vertx.eventBus().consumer("ipl.today", (message)->
        {
            System.out.println("Recieving thread " + Thread.currentThread().getName() + " recieves message " + message.body().toString());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            message.reply("Receiver thread " + Thread.currentThread().getName() + " replies to message " + message.body().toString());
        });
    }

    @Override
    public void stop()
    {

    }
}


