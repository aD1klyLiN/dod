package pes.dungeonofdoom;

/**
 * Created by lylin on 12.06.16.
 */
public class Charc {

    private String mCname; //Имя персонажа.
    private int[] mStratt; //Сила (0), бонус силы (1)
    private int[] mStaatt; //Выносливость (0), бонус выносливости (1)
    private int[] mDexatt; //Ловкость (0), бонус ловкости (1)
    private int[] mAglatt; //Проворство (0), бонус проворства (1)
    private int[] mIntatt; //Интеллект (0), бонус интеллекта (1)
    private int mCurrhp; //Текущее здоровье
    private int mMaxhp; //Максимальное здоровье
    private int[] mUcfsk; //Рукопашный бой (0), бонус рукопашного боя (1)
    private int[] mAcfsk; //Оружие ближнего боя 0), бонус оружия бл.боя (1)
    private int[] mPcfsk; //Дистанционное оружие (0), бонус дист. оружия (1)
    private int[] mMcfsk; //Магическая атака (0), бонус маг. атаки (1)
    private int[] mCdfsk; //Защита (0), бонус защиты (1)
    private int[] mMdfsk; //Магическая защита (0), бонус маг. защиты (1)
    private int mCurrxp; //Текущий, расходуемый опыт.
    private int mTotxp; //Общая сумма опыта, за время жизни персонажа.
    private int mCurrgold; //Текущее количество золота.
    private int mTotgold; //Общая сумма золота за время жизни персонажа.
    private int mLocX; //Текущая x координата персонажа на карте.
    private int mLocY; //Текущая y координата персонажа на карте.

    public Charc() {
        mCname = new String();
        mStratt = new int[2];
        mStaatt = new int[2];
        mDexatt = new int[2];
        mAglatt = new int[2];
        mIntatt = new int[2];
        mCurrhp = 0;
        mMaxhp = 0;
        mUcfsk = new int[2];
        mAcfsk = new int[2];
        mPcfsk = new int[2];
        mMcfsk = new int[2];
        mMcfsk = new int[2];
        mMdfsk = new int[2];
        mCurrxp = 0;
        mTotxp = 0;
        mCurrgold = 0;
        mTotgold = 0;
        mLocX = 0;
        mLocY = 0;

    }

    public void printStats() {

    }

    public Charc genCharacter() {
        Charc ret = new Charc();
        ret.mStratt[0] = Utils.rnd(1, 20);
        ret.mStaatt[0] = Utils.rnd(1, 20);
        ret.mDexatt[0] = Utils.rnd(1, 20);
        ret.mAglatt[0] = Utils.rnd(1, 20);
        ret.mIntatt[0] = Utils.rnd(1, 20);
        ret.mCurrhp = ret.mStratt[0] + ret.mStaatt[0];
        ret.mMaxhp = ret.mCurrhp;
        ret.mUcfsk[0] = ret.mStratt[0] + ret.mAglatt[0];
        ret.mAcfsk[0] = ret.mStratt[0] + ret.mDexatt[0];
        ret.mPcfsk[0] = ret.mDexatt[0] + ret.mIntatt[0];
        ret.mMcfsk[0] = ret.mIntatt[0] + ret.mStaatt[0];
        ret.mCdfsk[0] = ret.mDexatt[0] + ret.mAglatt[0];
        ret.mMdfsk[0] = ret.mAglatt[0] + ret.mIntatt[0];
        ret.mCurrxp = Utils.rnd(100, 200);
        ret.mTotxp = ret.mCurrxp;
        ret.mCurrgold = Utils.rnd(50, 100);
        ret.mTotgold = ret.mCurrgold;

        return ret;
    }

    public boolean inputName(String name) {
        if (name.length()<1||name.length()>40) {
            return false;
        } else {
            this.mCname = name;
            return true;
        }
    }
}
