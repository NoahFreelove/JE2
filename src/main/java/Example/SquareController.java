package Example;

import JE.Objects.Components.Component;
import JE.Objects.Square;

public class SquareController extends Component {
    private Square square;
    public SquareController(Square s){
        this.square = s;
    }
    @Override
    public void update() {
        if(square !=null)
            square.getTransform().position.x+=0.02f;
    }

    @Override
    public void start() {

    }

    @Override
    public void awake() {

    }
}
