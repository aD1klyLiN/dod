package pes.dungeonofdoom;

/**
 * класс описывает персонажа игры
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

    /**
     * конструктор
     */
    public Charc() {
        //mCname = new String();
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
        mCdfsk = new int[2];
        mMdfsk = new int[2];
        mCurrxp = 0;
        mTotxp = 0;
        mCurrgold = 0;
        mTotgold = 0;
        mLocX = 0;
        mLocY = 0;

    }

    /**
     * метод возвращает строковый массив со значениями характеристик персонажа
     * @return - готовый массив с характеристиками
     */
    public String[] printStats() {
        String[] attrs = new String[14];
        attrs[0] = Integer.toString(mStratt[0] + mStratt[1]);
        attrs[1] = Integer.toString(mStaatt[0] + mStaatt[1]);
        attrs[2] = Integer.toString(mDexatt[0] + mDexatt[1]);
        attrs[3] = Integer.toString(mAglatt[0] + mAglatt[1]);
        attrs[4] = Integer.toString(mIntatt[0] + mIntatt[1]);
        attrs[5] = Integer.toString(mCurrhp);
        attrs[6] = Integer.toString(mUcfsk[0] + mUcfsk[1]);
        attrs[7] = Integer.toString(mAcfsk[0] + mAcfsk[1]);
        attrs[8] = Integer.toString(mPcfsk[0] + mPcfsk[1]);
        attrs[9] = Integer.toString(mMcfsk[0] + mMcfsk[1]);
        attrs[10] = Integer.toString(mCdfsk[0] + mCdfsk[1]);
        attrs[11] = Integer.toString(mMdfsk[0] + mMdfsk[1]);
        attrs[12] = Integer.toString(mCurrxp);
        attrs[13] = Integer.toString(mCurrgold);
        return attrs;
    }

    /**
     * метод возвращает имя персонажа
     * @return - имя
     */
    public String getCname() {
        return mCname;
    }

    /**
     * метод генерирует персонажа со случайными характеристикиами
     * @param name - имя персонажа
     * @return - созданный персонаж
     */
    public Charc genCharacter(String name) {
        Charc ret = new Charc();
        ret.mCname = name;
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

}
