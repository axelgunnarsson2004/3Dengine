object Main:
  var camera = Camera(Vec3(0, 0, 3), 0, 0)
  @main 
  def run(): Unit = 
    while(true) {


      var key =Window.getKey()
      camera = Camera.updateCamera(camera,key)
      Window.window.clear()
      // Window.window.fill(0,0,Window.windowSize._1,Window.windowSize._2,java.awt.Color.blue)
      
      World.drawWorld(camera)

      Thread.sleep(16)
  } 
