package JE.Sample.Components;

import JE.IO.UserInput.Keyboard.Combos.ComboList;
import JE.IO.UserInput.Keyboard.Keyboard;
import JE.Objects.Components.Base.Component;
import org.jbox2d.common.Vec2;

import static JE.IO.UserInput.Keyboard.Combos.ComboList.*;
import static JE.Window.Window.deltaTime;

public class MovementController extends Component {

    public float moveSpeed = 5f;
    public float jumpVelocity = 5f;
    public boolean masterCanMove = true;
    public boolean enableJump = true;
    public boolean canMoveUp = true;
    public boolean canMoveDown = true;
    public boolean canMoveRight = true;
    public boolean canMoveLeft = true;

    public boolean physicsBased = true;
    public boolean absoluteXPositioning = true;
    public boolean absoluteYPositioning = false;

    volatile boolean isHugging = false;

    public MovementController(){}

    @Override
    public void update() {
        if(!masterCanMove)
            return;
        if(parentObject() == null)
            return;

        if(physicsBased){
            if(parentObject().physicsBody !=null){
                if(Keyboard.isComboPressed(LEFT) && canMoveLeft){
                    if(isHuggingWall()){
                        parentObject().physicsBody.body.setLinearVelocity(new Vec2(0,parentObject().physicsBody.body.getLinearVelocity().y));
                    }
                    else {
                        parentObject().physicsBody.body.setLinearVelocity(new Vec2(-moveSpeed,parentObject().physicsBody.body.getLinearVelocity().y));
                    }
                }
                if(Keyboard.isComboPressed(RIGHT) && canMoveRight){
                    if(isHuggingWall()){
                        parentObject().physicsBody.body.setLinearVelocity(new Vec2(0,parentObject().physicsBody.body.getLinearVelocity().y));
                    }
                    else {
                        parentObject().physicsBody.body.setLinearVelocity(new Vec2(moveSpeed,parentObject().physicsBody.body.getLinearVelocity().y));
                    }
                }

                if(absoluteXPositioning){
                    if(!Keyboard.isComboPressed(RIGHT) && !Keyboard.isComboPressed(LEFT)){
                        parentObject().physicsBody.body.setLinearVelocity(new Vec2(0,parentObject().physicsBody.body.getLinearVelocity().y));
                    }
                }
                if(absoluteYPositioning){
                    if(!Keyboard.isComboPressed(UP) && !Keyboard.isComboPressed(DOWN)){
                        parentObject().physicsBody.body.setLinearVelocity(new Vec2(parentObject().physicsBody.body.getLinearVelocity().x,0));
                    }
                }
                if(enableJump)
                {
                    if(Keyboard.isComboPressed(UP)){
                        if(parentObject().physicsBody.onGround)
                            parentObject().physicsBody.body.setLinearVelocity(new Vec2(parentObject().physicsBody.body.getLinearVelocity().x,jumpVelocity));
                    }
                }
                else {
                    if(canMoveUp){
                        if(Keyboard.isComboPressed(UP)){
                            parentObject().physicsBody.body.setLinearVelocity(new Vec2(parentObject().physicsBody.body.getLinearVelocity().x,moveSpeed));
                        }
                    }
                }
                if(canMoveDown){
                    if(Keyboard.isComboPressed(DOWN)){
                        parentObject().physicsBody.body.setLinearVelocity(new Vec2(parentObject().physicsBody.body.getLinearVelocity().x,-moveSpeed));
                    }
                }
            }
        }
        else{
            if(Keyboard.isComboPressed(LEFT) && canMoveLeft){
                parentObject().getTransform().translateX(-moveSpeed * deltaTime());
            }
            if(Keyboard.isComboPressed(RIGHT) && canMoveRight){
                parentObject().getTransform().translateX(moveSpeed * deltaTime());
            }
            if(Keyboard.isComboPressed(UP) && canMoveUp){
                parentObject().getTransform().translateY(moveSpeed * deltaTime());
            }
            if(Keyboard.isComboPressed(DOWN) && canMoveDown){
                parentObject().getTransform().translateY(-moveSpeed * deltaTime());
            }
        }
    }

    public boolean isHuggingWall(){
        return false;
    }
}
