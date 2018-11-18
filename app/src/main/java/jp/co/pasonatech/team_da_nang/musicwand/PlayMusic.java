package jp.co.pasonatech.team_da_nang.musicwand;

import java.util.Date;
import java.util.Random;

public class PlayMusic {
    public enum Key{
        C(0),
        Cs(1),
        Df(1),
        D(2),
        Ds(3),
        Ef(3),
        E(4),
        F(5),
        Fs(6),
        Gf(6),
        G(7),
        Gs(8),
        Af(8),
        A(9),
        As(10),
        Bf(10),
        B(11);

        private int Value;

        private Key(int Value){
            this.Value = Value ;
        }
        public int getInt(){
            return (int)this.Value ;
        }
    }

    public enum Scale{
        MajorDiatonic(10),
        MajorPentatonic(11),
        NaturalMinor(20),
        MinorPentatonic(21),
        HarmonicMinor(22),
        MerodicMinor(23),
        MajorBlues(30),
        BlueNote(31),
        Hungarian(100),
        Gipsy(110);

        private int Value;

        private Scale(int Value){
            this.Value = Value ;
        }
    }
    private static final PlayMusic.Scale scale = Scale.MajorDiatonic;

    protected PlayMusic.Key key;
    protected Instrument instrument;
    protected int octave;
    protected int topOctave;
    protected int bottumOctave;
    protected int octaveUp;
    protected int octaveDown;

    private static final int[] scaleTable = {0,2,4,5,7,9,11} ;
    protected boolean firstPitch ;
    protected int note ;
    protected Random random ;

    public PlayMusic(Instrument instrument, PlayMusic.Key key, int upper, int bottum){

        this.key = key ;
        this.octave = 60 / 12; // 60 : Center C
        this.topOctave = this.octave + upper;
        this.bottumOctave = this.octave - bottum;
        this.instrument = instrument;
        this.firstPitch = false ;
        this.octaveUp = 15;
        this.octaveDown = 15;

        Date date = new Date();
        this.random = new Random(date.getTime());
    }

    private int decideOvtave() {
        int num = this.random.nextInt(100);

        if (99 - this.octaveUp <= num) {
            if (this.topOctave > this.octave) {
                this.octave++;
            }
        } else if ( this.octaveDown >= num) {
            if (this.bottumOctave < this.octave) {
                this.octave--;
            }
        }
        return this.octave;
    }

    private int decideNote(){
        if( false == this.firstPitch ){
            this.firstPitch = true;
            this.note = this.key.getInt() ;    //  60 is center C.
        }
        this.note = this.key.getInt() + this.scaleTable[this.random.nextInt(scaleTable.length)];
        return this.note ;
    }

    public void Play(){
        this.instrument.allOff();
        this.decideOvtave();
        this.decideNote();
//        this.instrument.noteOn(this.decideNote(),this.random.nextInt(64)+64) ;
        this.instrument.noteOn(this.octave, this.note,this.random.nextInt(64)+64) ;
    }
    public PlayMusic.Key getKey(){
        return this.key;
    }
    public PlayMusic.Scale getScale(){
        return this.scale;
    }
    public int getOctave(){ return this.octave; }
    public int getOctaveUp() { return this.octaveUp ;}
    public void setOctaveUp( int up ){ this.octaveUp = up%100 ;}
    public int getOctaveDown() { return this.octaveDown ;}
    public void setOctaveDown( int down ){ this.octaveDown = down%100 ;}
}

class PlayMusicMajorPentatonic extends PlayMusic{
    private static final PlayMusic.Scale scale = Scale.MajorPentatonic;
    private static final int[] scaleTable = {0,2,4,7,9};

    public PlayMusicMajorPentatonic(Instrument instrument, PlayMusic.Key key, int upper, int bottum){
        super(instrument, key, upper, bottum);
        this.octaveUp = 30 ;
        this.octaveDown = 30 ;
    }
}

class PlayMusicNaturalMinor extends PlayMusic{
    private static final PlayMusic.Scale scale = Scale.NaturalMinor;
    private int[] scaleTable = {0,2,3,5,6,8,10};

    public PlayMusicNaturalMinor(Instrument instrument, PlayMusic.Key key, int upper, int bottum){
        super(instrument, key, upper, bottum);
        this.octaveUp = 10 ;
        this.octaveDown = 10 ;
    }
}

class PlayMusicHarmonicMinor extends PlayMusic{
    private static final PlayMusic.Scale scale = Scale.HarmonicMinor;
    private int[] scaleTable = {0,2,3,5,6,9,10};

    public PlayMusicHarmonicMinor(Instrument instrument, PlayMusic.Key key, int upper, int bottum){
        super(instrument, key, upper, bottum);
        this.octaveUp = 10 ;
        this.octaveDown = 10 ;
    }
}

class PlayMusicMerodicMinor extends PlayMusic{
    private static final PlayMusic.Scale scale = Scale.MerodicMinor;
    private int[][] scaleTable = {{0,2,3,5,7,9,11},{0,2,3,5,6,8,10}};

    public PlayMusicMerodicMinor(Instrument instrument, PlayMusic.Key key, int upper, int bottum){
        super(instrument, key, upper, bottum);
        this.octaveUp = 10 ;
        this.octaveDown = 10 ;
    }

    private int decideOvtave(boolean up) {
        int num = this.random.nextInt(100);

        if ((true == up) && (99 - this.octaveUp <= num)) {
            if (this.topOctave > this.octave) {
                this.octave++;
            }
        } else if ( this.octaveDown >= num) {
            if (this.bottumOctave < this.octave) {
                this.octave--;
            }
        }
        return this.octave;
    }

    private int decideNote(){
        int beforeNote = this.note;

        if( false == this.firstPitch ){
            this.firstPitch = true;
            this.note = this.key.getInt() ;    //  60 is center C.
        }
        else if( 0 != (this.random.nextInt()&1)){
            if( this.key.getInt() + this.scaleTable[0][this.scaleTable[0].length-1] <= this.note){
                if(this.octave < this.topOctave){
                    this.octave++ ;
                    this.note = this.key.getInt() + this.scaleTable[0][this.random.nextInt(scaleTable[0].length)];
                }
            }
            else {
  //              this.decideOvtave( true );
                do {
                    this.note = this.key.getInt() + this.scaleTable[0][this.random.nextInt(scaleTable[0].length)];
                } while (beforeNote > this.note);
            }
        }
        else{
            if( this.key.getInt() >= this.note){
                if(this.octave > this.bottumOctave){
                    this.octave-- ;
                    this.note = this.key.getInt() + this.scaleTable[1][this.random.nextInt(scaleTable[1].length)];
                }
            }
            else {
//                this.decideOvtave( false );
                do {
                    this.note = this.key.getInt() + this.scaleTable[1][this.random.nextInt(scaleTable[1].length)];
                } while (beforeNote < this.note);
            }
        }
        return this.note ;
    }
    public void Play(){
        this.instrument.allOff();
        this.decideNote();
        this.instrument.noteOn(this.octave, this.note,this.random.nextInt(64)+64) ;
    }
}

class PlayMusicMinorPentatonic extends PlayMusic{
    private static final PlayMusic.Scale scale = Scale.MinorPentatonic;
    private int[] scaleTable = {0,3,5,7,10};

    public PlayMusicMinorPentatonic(Instrument instrument, PlayMusic.Key key, int upper, int bottum){
        super(instrument, key, upper, bottum);
        this.octaveUp = 10 ;
        this.octaveDown = 10 ;
    }
}

class PlayMusicMajorBlues extends PlayMusic{
    private static final PlayMusic.Scale scale = Scale.MajorBlues;
    private int[] scaleTable = {0,2,3,5,7,9};

    public PlayMusicMajorBlues(Instrument instrument, PlayMusic.Key key, int upper, int bottum){
        super(instrument, key, upper, bottum);
    }
}

class PlayMusicBlueNote extends PlayMusic{
    private static final PlayMusic.Scale scale = Scale.BlueNote;
    private int[] scaleTable = {0,3,5,6,7,10};

    public PlayMusicBlueNote(Instrument instrument, PlayMusic.Key key, int upper, int bottum){
        super(instrument, key, upper, bottum);
    }
}

class PlayMusicHungarian extends PlayMusic{
    private static final PlayMusic.Scale scale = Scale.Hungarian;
    private int[] scaleTable = {0,2,3,6,7,8,11};

    public PlayMusicHungarian(Instrument instrument, PlayMusic.Key key, int upper, int bottum){
        super(instrument, key, upper, bottum);
        this.octaveUp = 10 ;
        this.octaveDown = 10 ;
    }
}

class PlayMusicGipsy extends PlayMusic{
    private static final PlayMusic.Scale scale = Scale.Gipsy;
    private int[] scaleTable = {0,1,4,5,7,8,11};

    public PlayMusicGipsy(Instrument instrument, PlayMusic.Key key, int upper, int bottum){
        super(instrument, key, upper, bottum);
        this.octaveUp = 10 ;
        this.octaveDown = 10 ;
    }
}
