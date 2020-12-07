import gmaths.*;
import com.jogamp.opengl.*;


public class Heli {

  private float bodyWidth = 0.2f;
  private float bodyDepth = 0.3f;
  private float bodyHeight = 0.2f;

  private float tailLength = 0.3f;
  private float tailSize = 0.07f;

  private float propRadius = 0.02f;
  private float propLength = 0.3f;

  private float rotorRadius = 0.075f;

  private float heliHeight = 0f;

  private Camera camera;
  private Light light;
  private Table table;

  private Model body, tail, rotor, prop1, prop2;
  private SGNode heliRoot;

  private TransformNode tailRotation, rotorRotation, prop1Rotation, prop2Rotation, heliRootTranslate;

  private float rotorAngle = 0f;
  private boolean propMotion = false;

  private float inc = 0.01f; //rotor accelleration
  private float vs = 0.0001f; //vertical speed

  private double startTime = getSeconds();

  public Heli(GL3 gl, Camera camera, Light light, Table table){

    this.camera = camera;
    this.light = light;
    this.table = table;

    int[] textureId0 = TextureLibrary.loadTexture(gl, "./textures/jade.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "./textures/chequerboard.jpg");

    body = initProp(gl, textureId1);
    prop1 = initProp(gl, textureId0);
    prop2 = initProp(gl, textureId0);
    tail = initBody(gl, textureId1);
    rotor = initProp(gl, textureId1);

    heliHeight = table.getTableHeight() + bodyHeight/2;

    heliRoot = new NameNode("helicopter Root");
    heliRootTranslate = new TransformNode("helicopter root transform", Mat4Transform.translate(0.4f,heliHeight,-1.5f));

    NameNode bodyName = new NameNode("bodyName");
      Mat4 m = Mat4Transform.scale(bodyWidth, bodyHeight, bodyDepth);
      TransformNode bodyScale = new TransformNode("body transform", m);
        ModelNode bodyShape = new ModelNode("Cube(0)", body);

    NameNode tailName = new NameNode("tailName");
      TransformNode tailTranslate = new TransformNode("tail translate", Mat4Transform.translate(0,0,-tailLength+0.05f));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(tailSize, tailSize, tailLength));
      TransformNode tailScale = new TransformNode("tail transform", m);
        ModelNode tailShape = new ModelNode("Cube(1)", tail);

    NameNode rotorName = new NameNode("rotorName");
      TransformNode rotorTranslate = new TransformNode("rotor translate", Mat4Transform.translate(0,bodyHeight/2,0));
      rotorRotation = new TransformNode("rotor rotation", Mat4Transform.rotateAroundY(rotorAngle));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(rotorRadius,rotorRadius, rotorRadius));
      TransformNode rotorScale = new TransformNode("rotor transform", m);
        ModelNode rotorShape = new ModelNode("SPhere(1)", rotor);

    NameNode prop1Name = new NameNode("prop1Name");
      TransformNode prop1Translate = new TransformNode("prop1 translate", Mat4Transform.translate(0,0,-propLength/2));
      prop1Rotation = new TransformNode("prop1 rotate 90", Mat4Transform.rotateAroundZ(90));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(propRadius, propRadius, propLength));
      TransformNode prop1Scale = new TransformNode("prop1 transform", m);
        ModelNode prop1Shape = new ModelNode("Sphere(0)", prop1);

    NameNode prop2Name = new NameNode("prop2Name");
      TransformNode prop2Translate = new TransformNode("prop2 translate", Mat4Transform.translate(0,0,propLength/2));
      prop2Rotation = new TransformNode("prop2 rotate 90", Mat4Transform.rotateAroundZ(90));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(propRadius, propRadius, propLength));
      TransformNode prop2Scale = new TransformNode("prop2 transform", m);
        ModelNode prop2Shape = new ModelNode("Sphere(0)", prop2);


    heliRoot.addChild(heliRootTranslate);
      heliRootTranslate.addChild(bodyName);
        bodyName.addChild(bodyScale);
        bodyScale.addChild(bodyShape);
      heliRootTranslate.addChild(tailName);
        tailName.addChild(tailTranslate);
        tailTranslate.addChild(tailScale);
        tailScale.addChild(tailShape);
      heliRootTranslate.addChild(rotorName);
        rotorName.addChild(rotorRotation);
        rotorRotation.addChild(rotorTranslate);
        rotorTranslate.addChild(rotorScale);
        rotorScale.addChild(rotorShape);
        rotorTranslate.addChild(prop1Name);
          prop1Name.addChild(prop1Rotation);
          prop1Rotation.addChild(prop1Translate);
          prop1Translate.addChild(prop1Scale);
          prop1Scale.addChild(prop1Shape);
        rotorTranslate.addChild(prop2Name);
          prop2Name.addChild(prop2Rotation);
          prop2Rotation.addChild(prop2Translate);
          prop2Translate.addChild(prop2Scale);
          prop2Scale.addChild(prop2Shape);

    heliRoot.update();
    //heliRoot.print(0,false);


  }

  private Model initBody(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_cube.txt", "./shaders/fs_cube.txt");
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model body = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return body;
  }

  private Model initProp(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_sphere.txt", "./shaders/fs_sphere.txt");
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model prop = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return prop;
  }

  public void draw(GL3 gl){
    heliRoot.draw(gl);
  }

  public void turnPropeller(){

    if(inc < 20f){inc = inc*(float)(Math.pow(1+0.2f,60));}
    rotorAngle = rotorAngle + inc;
    rotorRotation.setTransform(Mat4Transform.rotateAroundY(rotorAngle));
    rotorRotation.update();
  }

  public void heliFloat(float h, double aniTime){
    turnPropeller();
    if(heliHeight < h-0.05f){
      //VS While climbing
      if((h - heliHeight) < 0.5f){ //If Approaching the target height slow down ascent
        if(vs > 0.003f){
          vs = vs - 0.0005f;
        }
      }else{
        if(vs < 0.01f) {
          if(vs < 0){vs=0.001f;}
          vs=vs*(float)(Math.pow((1+0.001f),60));
        }
      }
      heliHeight = heliHeight + vs;
      heliRootTranslate.setTransform(Mat4Transform.translate(0.4f,heliHeight,-1.5f));
      heliRootTranslate.update();
    }else if(heliHeight > h+0.05){
      if(heliHeight - h < 0.5f){
        if(vs < -0.003f){
          vs = vs + 0.0005f;
        }
      }else{
        if(vs > -0.01f){
          vs=vs-0.0001f;
        }
      }
      heliHeight = heliHeight+vs;
      heliRootTranslate.setTransform(Mat4Transform.translate(0.4f,heliHeight,-1.5f));
      heliRootTranslate.update();
    }else{
        double elapsedTime = getSeconds()-aniTime;
        heliHeight = h + 0.05f * (float)Math.sin(elapsedTime);
        heliRootTranslate.setTransform(Mat4Transform.translate(0.4f,heliHeight,-1.5f));
        heliRootTranslate.update();

    }

  }



  public void heliLand(){
    float ground = table.getTableHeight()+bodyHeight/2;
    if(heliHeight > ground){
      heliFloat(ground, getSeconds());
    }
    shutPropeller();
  }

  public void shutPropeller(){
    if(heliHeight < table.getTableHeight()+bodyHeight/2) {
      if(inc > 20f){inc = inc/(float)(Math.pow(1+0.2f,60));}
      rotorAngle = rotorAngle + inc;
      rotorRotation.setTransform(Mat4Transform.rotateAroundY(rotorAngle));
      rotorRotation.update();
    }
  }

  public void dispose(GL3 gl){
    body.dispose(gl);
    tail.dispose(gl);
    rotor.dispose(gl);
    prop1.dispose(gl);
    prop2.dispose(gl);
  }


  private double getSeconds(){
    return System.currentTimeMillis()/1000.0;
  }

}
