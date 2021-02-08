package de.schmiereck.hexMap3D.service.reality;

public class Detector {
    private final int xPos;
    private final int yPos;
    private final int zPos;
    private final int xSize;
    private final int ySize;
    private final int xDetectorSize;
    private final int yDetectorSize;
    private final int zSize;
    private final DetectorCell[][] detectorCellArr;
    /**
     * z = 1:
     *     xPos yPos zPos
     * x = 1    0    0
     * y = 0    1    0
     *
     * y = 1:
     *     xPos yPos zPos
     * x = 1    0    0
     * y = 0    0    1
     *
     * x = 1:
     *     xPos yPos zPos
     * x = 0    1    0
     * y = 0    0    1
     */
    private final int[][] mapMatrix;

    public Detector(final int xPos, final int yPos, final int zPos, final int xSize, final int ySize, final int zSize) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        final int xs;
        final int ys;
        if (zSize <= 1) {
            this.mapMatrix = new int[][] { { 1, 0, 0 }, { 0, 1, 0 } };
            this.xDetectorSize = xSize;
            this.yDetectorSize = ySize;
        } else {
            if (ySize <= 1) {
                this.mapMatrix = new int[][] { { 1, 0, 0 }, { 0, 0, 1 } };
                this.xDetectorSize = xSize;
                this.yDetectorSize = zSize;
            } else {
                this.mapMatrix = new int[][] { { 0, 1, 0 }, { 0, 0, 1 } };
                this.xDetectorSize = zSize;
                this.yDetectorSize = ySize;
            }
        }
        //this.xDetectorSize = this.mapMatrix[0][0] * xSize + this.mapMatrix[0][1] * ySize + this.mapMatrix[0][2] * zSize;
        //this.yDetectorSize = this.mapMatrix[1][0] * xSize + this.mapMatrix[1][1] * ySize + this.mapMatrix[1][2] * zSize;
        this.detectorCellArr = new DetectorCell[this.xDetectorSize][this.yDetectorSize];
    }

    public int getXPos() {
        return this.xPos;
    }

    public int getYPos() {
        return this.yPos;
    }

    public int getZPos() {
        return this.zPos;
    }

    public int getXSize() {
        return this.xSize;
    }

    public int getYSize() {
        return this.ySize;
    }

    public int getZSize() {
        return this.zSize;
    }

    public int getXDetectorSize() {
        return this.xDetectorSize;
    }

    public int getYDetectorSize() {
        return this.yDetectorSize;
    }

    public DetectorCell[][] getDetectorCellArr() {
        return this.detectorCellArr;
    }

    public void setDetectorCell(final int xPos, final int yPos, final int zPos, final DetectorCell detectorCell) {
        final int x = this.mapMatrix[0][0] * xPos + this.mapMatrix[0][1] * yPos + this.mapMatrix[0][2] * zPos;
        final int y = this.mapMatrix[1][0] * xPos + this.mapMatrix[1][1] * yPos + this.mapMatrix[1][2] * zPos;
        this.detectorCellArr[x][y] = detectorCell;
    }

    public DetectorCell getDetectorCell(final int xPos, final int yPos) {
        return this.detectorCellArr[xPos][yPos];
    }
}
