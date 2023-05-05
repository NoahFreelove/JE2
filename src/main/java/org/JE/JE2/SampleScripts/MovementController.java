package org.JE.JE2.SampleScripts;

import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.Objects.Scripts.Script;
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

    public MovementController(){}

    private PhysicsBody physicsBody;
    @Override
    public void start(){
        physicsBody = getAttachedObject().getScript(PhysicsBody.class);
    }

    @Override
    public void update() {
        if(!masterCanMove)
            return;
        if(getAttachedObject() == null)
            return;

        if(physicsBased){
            if (physicsBody == null) {
                return;
            }
            if(Keyboard.isComboPressed(LEFT) && canMoveLeft){
                physicsBody.body.setLinearVelocity(new Vec2(-moveSpeed, physicsBody.body.getLinearVelocity().y));
            }
            if(Keyboard.isComboPressed(RIGHT) && canMoveRight){
                physicsBody.body.setLinearVelocity(new Vec2(moveSpeed, physicsBody.body.getLinearVelocity().y));
            }

            if(absoluteXPositioning){
                if(!Keyboard.isComboPressed(RIGHT) && !Keyboard.isComboPressed(LEFT)){
                    physicsBody.body.setLinearVelocity(new Vec2(0, physicsBody.body.getLinearVelocity().y));
                }
            }
            if(absoluteYPositioning){
                if(!Keyboard.isComboPressed(UP) && !Keyboard.isComboPressed(DOWN)){
                    physicsBody.body.setLinearVelocity(new Vec2(physicsBody.body.getLinearVelocity().x,0));
                }
            }
            if(enableJump)
            {
                if(Keyboard.isComboPressed(UP)){
                    if(physicsBody.onGround)
                        physicsBody.body.setLinearVelocity(new Vec2(physicsBody.body.getLinearVelocity().x,jumpVelocity));
                }
            }
            else {
                if(canMoveUp){
                    if(Keyboard.isComboPressed(UP)){
                        physicsBody.body.setLinearVelocity(new Vec2(physicsBody.body.getLinearVelocity().x,moveSpeed));
                    }
                }
            }
            if(canMoveDown){
                if(Keyboard.isComboPressed(DOWN)){
                    physicsBody.body.setLinearVelocity(new Vec2(physicsBody.body.getLinearVelocity().x,-moveSpeed));
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

}
