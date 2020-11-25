package de.schmiereck.hexMap3D.service;

public class ProbabilityCalc {
    // Init:
    public int valueRange = 12;
    public int probabilityBase = 100;
    public int probability;
    public int targetProbRange;

    // Range-Runtime:

    public int stepTicks;
    public int remainsProbRange = 0;

    // Tick-Runtime:

    public int tickPos = 0;
    public int rangePos;
    public int posInRange;
    private boolean execute = false;

    public ProbabilityCalc(final int valueRange, final int probabilityBase, final int probability) {
        this.valueRange = valueRange;
        this.probabilityBase = probabilityBase;
        this.probability = probability;
        this.targetProbRange = this.probability * this.valueRange;

        this.calcTickState();
    }

    /**
     * Copy and calc next Probability-Tick-Pos.
     */
    public ProbabilityCalc(final ProbabilityCalc probCalc) {
        // Init:
        this.valueRange = probCalc.valueRange;
        this.probabilityBase = probCalc.probabilityBase;
        this.probability = probCalc.probability;
        this.targetProbRange = probCalc.targetProbRange;
        // Range-Runtime:
        this.stepTicks = probCalc.stepTicks;
        this.remainsProbRange = probCalc.remainsProbRange;
        // Tick-Runtime:
        this.tickPos = probCalc.tickPos;
        this.rangePos = probCalc.rangePos;
        this.posInRange = probCalc.posInRange;
        this.execute = probCalc.execute;

        this.calcNextTick();
    }

    public void calcTickState() {
        this.rangePos = this.tickPos / this.valueRange;
        this.posInRange = this.tickPos % this.valueRange;

        if (this.posInRange == 0) {
            initNextRange();
        }

        if (this.stepTicks != 0) {
            final int stepPos = this.tickPos % this.stepTicks;

            if (stepPos == 0) {
                this.execute = true;
            } else {
                this.execute = false;
            }
        } else {
            this.execute = false;
        }
    }

    public void calcNextTick() {
        this.tickPos++;

        this.calcTickState();
    }

    private void initNextRange() {
        // Zähler:	  0 1 2 3 4 5 6 7 8 9 0 1  => Range 12

        // Aktion 1: 0 . . 1 . . 3 . . 4 . .  40% -> alle 3 (12/4=3) (12/9=1)
        // Aktion 2: . 0 . . . 1 . . . 2 . .  30% -> alle 4 (12/3=4)
        // Aktion 3: . . . 0 . . . 1 . . . 2  30% -> alle 4 (12/3=4)
        // x1 = 40% * 12 / 100%                   = 480 / 100%  = 4 Rest 80% / 100%

        // Aktion 1: 0 . 1 . 2 . 3 . 4 . 5 .  50% -> alle 2 (12/6=2)
        // x  = 50% * 12 / 100%         = 600% / 100%  = 6 Rest 0

        // 40%:	4,8=40%*12		8,333=100/12 	33,333=8,333*4	41,666=8,333*5%	6,666=2*3,333	6,666=4*1,666
        // 0: 480%=40%*12		4=480%/100%	3=12/4	400%=4*100%	80%=480%-400%
        // 1: 560%=40%*12+80%	5=560%/100%	2=12/5	500%=5*100%	60%=480%-500%+80
        // 2: 540%=40%*12+60%	5=540%/100%	2=12/5	500%=5*100%	40%=480%-500%+60%
        // 3: 520%=40%*12+40%	5=520%/100%	2=12/5	500%=5*100%	20%=480%-500%+40%
        // 4: 500%=40%*12+20%	5=500%/100%	2=12/5	500%=5*100%	0%=480%-500%+20%
        // 5: 480%=40%*12+0%	4=480%/100%	3=12/4	400%=4*100%	80%=480%-400%

        // xx%: 1200=100%*12
        final int maxPropRange = this.probabilityBase * this.valueRange;
        // 30%: 360%=30%*12
        // 40%: 480%=40%*12
        // 50%: 600%=50%*12
        // 60%: 720%=60%*12
        final int propRange = this.probability * this.valueRange + this.remainsProbRange;
        // 30%: 3=360%/100%
        // 40%: 4=480%/100% 5=560%/100%
        // 50%: 6=600%/100%
        // 60%: 7=720%/100%
        final int countPerRange = (propRange) / this.probabilityBase;
        // 30%: (4=12/3)
        // 40%: (3=12/4)    2=12/5
        // 50%: (2=12/6)
        // 60%: (1=12/7)
        if (countPerRange != 0) {
            this.stepTicks = this.valueRange / countPerRange;
        } else {
            this.stepTicks = 0;
        }
        // 30%: 300=3*100
        // 40%: 400=4*100   500=5*100
        // 50%: 600=6*100
        // 60%: 700=7*100
        final int realPropRange = countPerRange * this.probabilityBase;
        // 30%: 60=360-300;
        // 40%: 80=480-400; -20=480-500
        // 50%: 0=600-600;
        // 60%: 20=720-700;
        this.remainsProbRange = this.targetProbRange - realPropRange + this.remainsProbRange;
    }

    public boolean getExecute() {
        return this.execute;
    }
}
