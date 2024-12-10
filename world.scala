import java.awt.Color as JColor
object World:
  


  val cubeGrid = Array(JColor.red,JColor.orange,JColor.yellow,JColor.green,JColor.blue)
  val d2cubegrid = Array(cubeGrid,cubeGrid,cubeGrid,cubeGrid,cubeGrid)
  val d3cubeGrid = Array(d2cubegrid,d2cubegrid,d2cubegrid,d2cubegrid,d2cubegrid)

  def vecAdd(v1: Vec3, v2: Vec3,i: Int=1): Vec3 =
    Vec3(v1._1 + v2._1*i, v1._2 + v2._2*i, v1._3 + v2._3*i)

  val cubePoints: List[Vec3] = List(
      Vec3(-1, 1, 1),  // Top-left front:
      Vec3(1, 1, 1),   // 1: Top-right front
      Vec3(1, -1, 1),  // 2: Bottom-right front
      Vec3(-1, -1, 1), // 3: Bottom-left front
      Vec3(-1, 1, -1), // 4: Top-left back
      Vec3(1, 1, -1),  // 5: Top-right back
      Vec3(1, -1, -1), // 6: Bottom-right back
      Vec3(-1, -1, -1) // 7: Bottom-left back
    )

    // Define the edges of the cube as pairs of point indices
    val edges: List[(Int, Int)] = List(
      // Front face
      (0, 1), (1, 2), (2, 3), (3, 0),
      // Back face
      (4, 5), (5, 6), (6, 7), (7, 4),
      // Connecting edges between front and back faces
      (0, 4), (1, 5), (2, 6), (3, 7)
    )

    def drawCube(camera: Camera,cubeP: List[Vec3], color: JColor = JColor.red): Unit = 
      for ((startIdx, endIdx) <- edges) {
        val start = cubeP(startIdx)
        val end = cubeP(endIdx)
        Window.line(start, end,camera,color)
      }

    def drawWorld(camera: Camera): Unit = 
      // for i <- 0 until cubeGrid.length do 
      //   drawCube(camera,cubePoints.map(vecAdd(_,Vec3(2,0,0),i)),cubeGrid(i))
      superCube(camera)
      // for i <- 0 until 100 do
      //   drawCube(camera,cubePoints.map(vecAdd(_,Vec3(2,0,0),i)),cubeGrid(i%5))
       
    def superCube(camera: Camera): Unit = 
      for i <- 0 until 5 do 
        for j <- 0 until 5 do 
          for k <- 0 until 5 do 
            var x = cubePoints.map(vecAdd(_,Vec3(2,0,0),i))
            var y = x.map(vecAdd(_,Vec3(0,2,0),j))
            var z = y.map(vecAdd(_,Vec3(0,0,2),k))
            drawCube(camera,z,d3cubeGrid(i)(j)(k))
