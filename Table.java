import gmaths.*;
import com.jogamp.opengl.*;


public class Table {

  private float tableWidth = 2f;
  private float tableDepth = 1f;
  private float tableHeight = 1f;
  private float tableLegSize = 0.1f;
  private float tableTopHeight = 0.1f;

  private Model top, leg0, leg1, leg2, leg3;
  private SGNode tableRoot;

  private Camera camera;
  private Light light;

  public Table(GL3 gl, Camera camera, Light light){

    this.camera = camera;
    this.light = light;

    int[] textureId0 = TextureLibrary.loadTexture(gl, "./textures/wall.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "./textures/floor.jpg");

    top = initLeg(gl, textureId0);
    leg0 = initLeg(gl, textureId1);
    leg1 = initLeg(gl, textureId1);
    leg2 = initLeg(gl, textureId1);
    leg3 = initLeg(gl, textureId1);



    tableRoot = new NameNode("tableRoot");
    TransformNode tableTopTranslate = new TransformNode("table transform", Mat4Transform.translate(0, tableHeight-0.1f, -2.0f+tableDepth/2));

    NameNode tableTopName = new NameNode("tableTop");
      Mat4 m = Mat4Transform.scale(tableWidth, tableTopHeight, tableDepth);
      m = Mat4.multiply(m, Mat4Transform.translate(0, tableHeight, 0));
      TransformNode tableTransform = new TransformNode("tableTop transform", m);
        ModelNode tableTopShape = new ModelNode("Plane(top)", top);


    NameNode tableLeg0Name = new NameNode("table leg0");
      TransformNode tableLeg0Translate = new TransformNode("tableleg translate",
                                            Mat4Transform.translate((-tableWidth/2+tableLegSize/2),tableHeight/2+0.1f,(-tableDepth/2+tableLegSize/2)));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(tableLegSize, tableHeight, tableLegSize));
      m = Mat4.multiply(m, Mat4Transform.translate(0,-tableHeight,0));
      TransformNode tableLeg0Scale = new TransformNode("TopLeft leg scale", m);
        ModelNode tableLeg0Shape = new ModelNode("Cube(1)", leg0);


    NameNode tableLeg1Name = new NameNode("table leg1");
      TransformNode tableLeg1Translate = new TransformNode("tableleg translate",
                                            Mat4Transform.translate((-tableWidth/2+tableLegSize/2),tableHeight/2+0.1f,(tableDepth/2-tableLegSize/2)));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(tableLegSize, tableHeight, tableLegSize));
      m = Mat4.multiply(m, Mat4Transform.translate(0,-tableHeight,0));
      TransformNode tableLeg1Scale = new TransformNode("BottomLeft leg scale", m);
        ModelNode tableLeg1Shape = new ModelNode("Cube(1)", leg1);


    NameNode tableLeg2Name = new NameNode("table leg2");
      TransformNode tableLeg2Translate = new TransformNode("tableleg translate",
                                            Mat4Transform.translate((tableWidth/2-tableLegSize/2),tableHeight/2+0.1f,(tableDepth/2-tableLegSize/2)));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(tableLegSize, tableHeight, tableLegSize));
      m = Mat4.multiply(m, Mat4Transform.translate(0,-tableHeight,0));
      TransformNode tableLeg2Scale = new TransformNode("BottomRight leg scale", m);
        ModelNode tableLeg2Shape = new ModelNode("Cube(2)", leg2);


    NameNode tableLeg3Name = new NameNode("table leg3");
      TransformNode tableLeg3Translate = new TransformNode("tableleg translate",
                                            Mat4Transform.translate((tableWidth/2-tableLegSize/2),tableHeight/2+0.1f,(-tableDepth/2+tableLegSize/2)));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(tableLegSize, tableHeight, tableLegSize));
      m = Mat4.multiply(m, Mat4Transform.translate(0,-tableHeight,0));
      TransformNode tableLeg3Scale = new TransformNode("TopRight leg scale", m);
        ModelNode tableLeg3Shape = new ModelNode("Cube(3)", leg3);


      tableRoot.addChild(tableTopTranslate);
        tableTopTranslate.addChild(tableTopName);
          tableTopName.addChild(tableTransform);
            tableTransform.addChild(tableTopShape);
          tableTopName.addChild(tableLeg0Name);
            tableLeg0Name.addChild(tableLeg0Translate);
            tableLeg0Translate.addChild(tableLeg0Scale);
              tableLeg0Scale.addChild(tableLeg0Shape);
          tableTopName.addChild(tableLeg1Name);
            tableLeg1Name.addChild(tableLeg1Translate);
            tableLeg1Translate.addChild(tableLeg1Scale);
              tableLeg1Scale.addChild(tableLeg1Shape);
          tableTopName.addChild(tableLeg2Name);
            tableLeg2Name.addChild(tableLeg2Translate);
            tableLeg2Translate.addChild(tableLeg2Scale);
              tableLeg2Scale.addChild(tableLeg2Shape);
          tableTopName.addChild(tableLeg3Name);
            tableLeg3Name.addChild(tableLeg3Translate);
            tableLeg3Translate.addChild(tableLeg3Scale);
              tableLeg3Scale.addChild(tableLeg3Shape);
      tableRoot.update();


  }


  public void draw(GL3 gl){
    tableRoot.draw(gl);
  }



  private Model initLeg(GL3 gl, int[] t){
      Mesh m = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
      Shader shader = new Shader(gl, "./shaders/vs_cube.txt", "./shaders/fs_cube.txt");
      Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
      Model leg = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
      return leg;
  }


  private Model initTop(GL3 gl, int[] t){
    Mesh m = new Mesh(gl, Plane.vertices.clone(), Plane.indices.clone());
    Shader shader = new Shader(gl, "./shaders/vs_cube.txt", "./shaders/fs_cube.txt");
    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), 1.0f);
    Model plane = new Model(gl, camera, light, shader, material, new Mat4(1), m, t);
    return plane;
  }

  public float getTableHeight(){
    return this.tableHeight + this.tableTopHeight/2;
  }


}
