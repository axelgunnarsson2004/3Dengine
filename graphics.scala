import java.awt.Color as JColor
case class Vec3(x: Float, y: Float, z: Float)
case class Vec2(x: Float, y: Float)

case class Camera(position: Vec3, yaw: Float, pitch: Float)


object Camera{
  var speed = -1f
  val scrollSpeed = 0.1f
  
  def move(camera: Camera, direction: Vec3, distance: Float): Camera = {
    Camera(
      Vec3(
        camera.position.x + direction.x * distance,
        camera.position.y + direction.y * distance,
        camera.position.z + direction.z * distance
        ),
        camera.yaw,
        camera.pitch
      )
  }
  def adjustYaw(camera: Camera, deltaYaw: Float): Camera =
    camera.copy(yaw=camera.yaw+deltaYaw)
  
  def adjustPitch(camera: Camera, deltaPitch: Float): Camera = 
    camera.copy(pitch=camera.pitch+deltaPitch)
  
  def negateVector(v : Vec3): Vec3 = 
    Vec3(-v.x,-v.y,-v.y)


  def pointingVector(camera: Camera): Vec3 = 
    val cosPitch = math.cos(camera.pitch).toFloat
    val sinPitch = math.sin(camera.pitch).toFloat
    val cosYaw = math.cos(camera.yaw).toFloat
    val sinYaw = math.sin(camera.yaw).toFloat

    // Pointing vector calculation
    Vec3(
      cosPitch * sinYaw, // x-component
      sinPitch,          // y-component
      cosPitch * cosYaw  // z-component
    ) 
  def updateCamera(camera: Camera, key: String): Camera = 

    var new_camera = camera
    
  

    if key == "w" then
      new_camera = Camera.move(camera,Vec3(0,0,1),speed)
    else if key == "s" then
      new_camera = Camera.move(camera,Vec3(0,0,-1),speed)
    else if key == "a" then
      new_camera = Camera.move(camera,Vec3(1,0,0),speed)
    else if key == "d" then
      new_camera = Camera.move(camera,Vec3(-1,0,0),speed)
     
    else if key == "Shift" then
      new_camera = Camera.move(camera,Vec3(0,-1,0),speed)
    else if key == " " then
      new_camera = Camera.move(camera,Vec3(0,1,0),speed)

    else if key == "Up" then
      new_camera = Camera.adjustPitch(camera, scrollSpeed)
    else if key == "Down" then
      new_camera = Camera.adjustPitch(camera, -scrollSpeed)
    else if key == "Right" then
      new_camera = Camera.adjustYaw(camera, scrollSpeed)
    else if key == "Left" then
      new_camera = Camera.adjustYaw(camera, -scrollSpeed)
  
    new_camera
}


object PerspectiveMatrix {
  val fov: Float = math.toRadians(90).toFloat // 90-degree field of view
  val near: Float = 0.1f                       // near clipping plane
  val far: Float = 100.0f                      // far clipping plane
  val height : Float = 1080f
  val width : Float = 1920f

  val aspect : Float = width / height
  // Calculate the aperspective projection matrix elements
  val f: Float = 1.0f / math.tan(fov / 2).toFloat
  val rangeInv: Float = 1.0f / (near - far)

  // The 4x4 matrix in flattened form
  val matrix: Array[Array[Float]] = Array(
    Array(f / aspect, 0, 0, 0),
    Array(0, f, 0, 0),
    Array(0, 0, (far + near) * rangeInv, 2 * far * near * rangeInv),
    Array(0, 0, -1f, 0)
  )

  def yawRotationMatrix(yaw: Float): Array[Array[Float]] = 
    val cosYaw = math.cos(yaw).toFloat
    val sinYaw = math.sin(yaw).toFloat
    Array(
      Array(cosYaw,0,sinYaw),
      Array(0,1f,0),
      Array(-sinYaw,0,cosYaw)
      )
  
  def pitchRotationMatrix(pitch: Float): Array[Array[Float]] = 
    val cosPitch = math.cos(pitch).toFloat
    val sinPitch = math.sin(pitch).toFloat
    Array(
      Array(1f,0,0),
      Array(0,cosPitch,-sinPitch),
      Array(0,sinPitch,cosPitch)
    )

  def transform(v: Vec2): Vec2 =
    val screenX = ((v.x + 1)/ 2f)*width
    val screenY = ((v.y + 1)/ 2f)* height
    Vec2(screenX,screenY)

  def applyRotation(point: Vec3, rotationMatrix: Array[Array[Float]]): Vec3 = {
    val x = point.x * rotationMatrix(0)(0) + point.y * rotationMatrix(0)(1) + point.z * rotationMatrix(0)(2)
    val y = point.x * rotationMatrix(1)(0) + point.y * rotationMatrix(1)(1) + point.z * rotationMatrix(1)(2)
    val z = point.x * rotationMatrix(2)(0) + point.y * rotationMatrix(2)(1) + point.z * rotationMatrix(2)(2)
    Vec3(x, y, z) 
  }



  def multiplyVec3(point: Vec3,camera : Camera): Vec2 = {
    // Multiply the 3D point by the perspective projection matrix
       // Perform perspective division
    var transformedPoint = Vec3(
      point.x - camera.position.x,
      point.y - camera.position.y,
      point.z - camera.position.z
    )

    // Multiply the 3D point by the perspective projection matrix (as before)
    transformedPoint = applyRotation(transformedPoint,yawRotationMatrix(camera.yaw))
    transformedPoint = applyRotation(transformedPoint,pitchRotationMatrix(camera.pitch))


    val x = transformedPoint.x * matrix(0)(0)
    val y = transformedPoint.y * matrix(1)(1)
    val z = transformedPoint.z * matrix(2)(2) + matrix(2)(3)
    val w = transformedPoint.z * matrix(3)(2)
    
   
    val ndcX = x / w
    val ndcY = y / w

    // Convert from NDC (-1 to 1) to screen space (0 to 1) if needed
    Vec2(ndcX, ndcY)
  }
  def normalize(point: Vec3, camera: Camera): Vec2 = 
    transform(multiplyVec3(point,camera))
}


object Window:
  import introprog.PixelWindow
  val windowSize = (1920,1080)
  val window = PixelWindow(windowSize._1,windowSize._2,"3D Engine")
  

 


  
  def line(p1 : Vec3, p2 : Vec3, camera : Camera, color : JColor = JColor.green) : Unit = 
    


    var a = PerspectiveMatrix.normalize(p1,camera)
    var b = PerspectiveMatrix.normalize(p2,camera)
    window.line(a._1.toInt,a._2.toInt,b._1.toInt,b._2.toInt,color,lineWidth=1)

  def getKey(): String = 
    window.awaitEvent()
    if window.lastEventType == PixelWindow.Event.KeyPressed then
      window.lastKey
    else 
      "ingen"






