package chronoSimulator;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

class NumberButtonHandler implements EventHandler<ActionEvent> {
    private final int number ;
    NumberButtonHandler(int number) {
        this.number = number ;
    }
    @Override
    public void handle(ActionEvent event) {
        // TODO numpad functionality
    	System.out.println(number);
    }

}