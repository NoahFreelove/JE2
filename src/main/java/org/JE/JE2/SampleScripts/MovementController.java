package org.JE.JE2.SampleScripts;

import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.Objects.Scripts.Base.Script;
import org.JE.JE2.Objects.Scripts.Physics.PhysicsBody;
import org.jbox2d.common.Vec2;

import static org.JE.JE2.IO.UserInput.Keyboard.Combos.ComboList.*;
import static org.JE.JE2.Window.Window.deltaTime;

public class MovementController extends Script {

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

    private PhysicsBody parentBody;
    @Override
    public void start(){
        parentBody = getAttachedObject().getScript(PhysicsBody.class);
    }

    @Override
    public void update() {
        if(!masterCanMove)
            return;
        if(getAttachedObject() == null)
            return;

        if(physicsBased){
            if(parentBody!=null){
                if(Keyboard.isComboPressed(LEFT) && canMoveLeft){
                    if(isHuggingWall()){
                        parentBody.body.setLinearVelocity(new Vec2(0, parentBody.body.getLinearVelocity().y));
                    }
                    else {
                        parentBody.body.setLinearVelocity(new Vec2(-moveSpeed, parentBody.body.getLinearVelocity().y));
                    }
                }
                if(Keyboard.isComboPressed(RIGHT) && canMoveRight){
                    if(isHuggingWall()){
                        parentBody.body.setLinearVelocity(new Vec2(0, parentBody.body.getLinearVelocity().y));
                    }
                    else {
                        parentBody.body.setLinearVelocity(new Vec2(moveSpeed, parentBody.body.getLinearVelocity().y));
                    }
                }

                if(absoluteXPositioning){
                    if(!Keyboard.isComboPressed(RIGHT) && !Keyboard.isComboPressed(LEFT)){
                        parentBody.body.setLinearVelocity(new Vec2(0, parentBody.body.getLinearVelocity().y));
                    }
                }
                if(absoluteYPositioning){
                    if(!Keyboard.isComboPressed(UP) && !Keyboard.isComboPressed(DOWN)){
                        parentBody.body.setLinearVelocity(new Vec2(parentBody.body.getLinearVelocity().x,0));
                    }
                }
                if(enableJump)
                {
                    if(Keyboard.isComboPressed(UP)){
                        if(parentBody.onGround)
                            parentBody.body.setLinearVelocity(new Vec2(parentBody.body.getLinearVelocity().x,jumpVelocity));
                    }
                }
                else {
                    if(canMoveUp){
                        if(Keyboard.isComboPressed(UP)){
                            parentBody.body.setLinearVelocity(new Vec2(parentBody.body.getLinearVelocity().x,moveSpeed));
                        }
                    }
                }
                if(canMoveDown){
                    if(Keyboard.isComboPressed(DOWN)){
                        parentBody.body.setLinearVelocity(new Vec2(parentBody.body.getLinearVelocity().x,-moveSpeed));
                    }
                }
            }
        }
        else{
            if(Keyboard.isComboPressed(LEFT) && canMoveLeft){
                getAttachedObject().getTransform().translateX(-moveSpeed * deltaTime());
            }
            if(Keyboard.isComboPressed(RIGHT) && canMoveRight){
                getAttachedObject().getTransform().translateX(moveSpeed * deltaTime());
            }
            if(Keyboard.isComboPressed(UP) && canMoveUp){
                getAttachedObject().getTransform().translateY(moveSpeed * deltaTime());
            }
            if(Keyboard.isComboPressed(DOWN) && canMoveDown){
                getAttachedObject().getTransform().translateY(-moveSpeed * deltaTime());
            }
        }
    }

    public boolean isHuggingWall(){
        return false;
    }
}
