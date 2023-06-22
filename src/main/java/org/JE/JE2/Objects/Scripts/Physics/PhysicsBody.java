package org.JE.JE2.Objects.Scripts.Physics;

import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Script;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.Utility.MethodTimer;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.RayCastInput;
import org.jbox2d.collision.RayCastOutput;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;

public class PhysicsBody extends Script {
    public transient org.jbox2d.dynamics.BodyDef bodyDef;
    public transient org.jbox2d.dynamics.Body body;
    public transient Fixture activeFixture;
    protected transient boolean hasInitialized = false;
    public transient boolean onGround = false;
    public boolean fixedRotation = true;
    private Vector2f size = new Vector2f(1,1);
    private BodyType mode = BodyType.DYNAMIC;

    protected Scene attachedScene = null;

    public float defaultRestitution = 0;
    public float defaultDensity = 1;
    public float defaultFriction = 1f;
    public float defaultGravity = 1f;
    private Vector2f defaultSize = new Vector2f();

    public PhysicsBody(){
        super();
    }
    
    protected PhysicsBody create(BodyType defaultState, Vector2f initialPosition, Vector2f initialSize){
        bodyDef = new BodyDef();
        this.size = new Vector2f(initialSize);
        bodyDef.type = defaultState;


        Vector2f adjustedPos = new Vector2f(initialPosition);
        adjustedPos.x += getSize().x/2;
        adjustedPos.y += getSize().y/2;
        bodyDef.position.set(adjustedPos.x(),adjustedPos.y());
        body = attachedScene.world.physicsWorld.createBody(this.bodyDef);
        body.setTransform(new Vec2(adjustedPos.x(),adjustedPos.y()), 0);
        body.setGravityScale(defaultGravity);
        // Create box shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(initialSize.x/2*1, initialSize.y/2);

        activeFixture = body.createFixture(shape, 1.0f);
        activeFixture.setRestitution(defaultRestitution);
        activeFixture.setDensity(defaultDensity);
        activeFixture.m_friction = defaultFriction;
        hasInitialized = true;
        return this;
    }

    public void setSize(float x, float y){
        if(!hasInitialized)
            return;
        body.destroyFixture(activeFixture);
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(x/2*1,y/2);
        shape.setRadius(0.001f);
        this.size = new Vector2f(x,y);
        activeFixture = body.createFixture(shape, 1.0f);
    }

    public PhysicsBody setMode(BodyType type){
        if(!hasInitialized)
        {
            mode = type;
            return this;
        }
        bodyDef.type = type;
        this.mode = type;
        body.setType(type);
        return this;
    }
    public void setFriction(float v){
        if(!hasInitialized)
            return;

        activeFixture.setFriction(v);
    }

    @Override
    public void start() {
        updateOnScriptUpdate = false;
        if(attachedScene == null)
            attachedScene = Manager.activeScene();

        if(!hasInitialized()){
            create(mode, getAttachedObject().getTransform().position(), defaultSize);
        }
        body.setUserData(getAttachedObject());
    }

    @Override
    public void onAddedToGameObject(GameObject gameObject) {
        if(body!=null)
            body.setUserData(gameObject);
        defaultSize = new Vector2f(gameObject.getTransform().scale());
    }

    private final Vector2f adjustedPos = new Vector2f();
    private final Vector2f pos = new Vector2f();
    private final Vec2 bodyPos = new Vec2();
    @Override
    public void update() {
        if(!hasInitialized)
            return;
        if(attachedScene != null){
            if(!isInCorrectScene())
                return;
        }

        if (body !=null)
        {
            if(mode == BodyType.STATIC)
                return;
            bodyPos.set(body.getPosition());

            pos.set(bodyPos.x,bodyPos.y);

            adjustedPos.set(pos);

            adjustedPos.x -= getSize().x /2;
            adjustedPos.y -= getSize().y/2;

            getAttachedObject().getTransform().setPosition(adjustedPos);

            if(!fixedRotation)
                getAttachedObject().getTransform().setRotation(0,0, body.getAngle());

            onGround = false;
            if(body.getType() == BodyType.DYNAMIC){
                // check if on ground directly below
                AABB aabb = new AABB();
                Vec2 pos2 = body.getPosition();
                pos2.y -= 0.5f;
                aabb.lowerBound.set(pos2);
                // Somehow this works, but I don't know why...
                pos2.y -= 0.2f;
                aabb.upperBound.set(pos2);
                Manager.activeScene().world.physicsWorld.queryAABB((fixture) -> {
                    if(fixture.getBody() != body)
                    {
                        if(fixture.m_isSensor)
                        {
                            return true;
                        }
                        onGround = true;
                        return true;
                    }
                    return false;
                }, aabb);
            }
        }
    }

    public Vector2f getSize() {
        return size;
    }
    public boolean hasInitialized(){return hasInitialized;}

    Vec2 jBoxStart = new Vec2();
    Vec2 jBoxEnd = new Vec2();
    public Raycast raycast(Vector2f start, Vector2f end, int layerMask){
        // Do a box 2d Raycast
        jBoxStart.set(start.x(),start.y());
        jBoxEnd.set(end.x(),end.y());
        RayCastInput input = new RayCastInput();
        input.p1.set(jBoxStart);
        input.p2.set(jBoxEnd);
        input.maxFraction = 1;

        float closestFraction = 1;
        org.jbox2d.dynamics.Fixture closestFixture = null;
        GameObject userData = null;
        org.jbox2d.collision.RayCastOutput output = new org.jbox2d.collision.RayCastOutput();
        for(Body b = Manager.activeScene().world.physicsWorld.getBodyList(); b != null; b = b.getNext()){
            if(b.getUserData() != null && b.getUserData() instanceof GameObject localData){
                // ignore if hit self
                if(localData == getAttachedObject())
                    continue;

                RayCastOutput tempOutput = new RayCastOutput();
                if(b.getFixtureList().raycast(tempOutput, input, 0) && tempOutput.fraction < closestFraction) {
                    if (localData.getLayer() == layerMask)
                    {
                        closestFixture = b.getFixtureList();
                        userData = (org.JE.JE2.Objects.GameObject) b.getUserData();
                        output.fraction = tempOutput.fraction;
                        output.normal.set(tempOutput.normal);
                        break;
                    }
                }
            }
        }
        if(closestFixture != null){
            return new Raycast(true, userData, new Vector2f(output.normal.x,output.normal.y));
        }
        return new Raycast(false, null, null);
    }

    public void setGravity(float newScale){
        if(!hasInitialized)
        {
            defaultGravity = newScale;
            return;
        }
        body.setGravityScale(newScale);
    }

    /**
     * Clones the physics body and adds it to the new scene. Used for DontDestroyOnLoad
     * @param scene The scene to add the new body to
     */
    public void cloneAndAdd(Scene scene){
        PhysicsBody pb = new PhysicsBody();
        pb.defaultDensity = defaultDensity;
        pb.defaultFriction = defaultFriction;
        pb.defaultGravity = defaultGravity;
        pb.defaultRestitution = defaultRestitution;
        pb.fixedRotation = fixedRotation;
        pb.mode = mode;
        pb.size = size;
        pb.attachedScene = scene;
        getAttachedObject().addScript(pb);
        pb.create(mode, getAttachedObject().getTransform().position(), getAttachedObject().getTransform().scale());
    }

    public boolean isInCorrectScene(){
        if(attachedScene == null)
            return false;
        //System.out.println(Manager.activeScene().name + " : " + attachedScene.name);

        return Manager.activeScene() == attachedScene;
    }
}
