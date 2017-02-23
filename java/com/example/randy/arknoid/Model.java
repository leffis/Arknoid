package com.example.randy.arknoid;

import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

import android.util.Log;
import android.util.AttributeSet;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.PopupWindow;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import android.content.Context;

import android.support.v7.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import java.util.Map;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.locks.ReentrantLock;

public class Model extends View {
    /* Map Chooser UI */
    private PopupWindow mapChooserPopup = null;

    /* External Defined MetaData */
    private AppCompatActivity mainActivity = null;  // the activity which create this model
    private String mapFname1 = null;                 // the name/path of the map info, not used yet
    private String mapFname2 = null;
    private Context context;                        // android UI shit
    private long seed = 123456789;                  // seed to generate random number
    private Random randNum;                         // random number to use

    public int paddleType;
    public int paddleType2;

    /* Internal Defined MetaData */
    private Mode gameMode = null;                   // define the game mode
    public ItemFactory itemFactory = null;         // instance of ItemFactory
    public int fps = 64;                            // frequency per second, fixed for now
    public final Boolean sos = false;               // trace case switch
    private MapLoader mapLoader = null;
    private Boolean maploaded = false;

    /* Canvas Data */
    private Bitmap bitmap;                          // android UI shit
    private Canvas canvas;                          // android UI shit, this is not the canvas we are painting
    public int width;                               // width of the game screen
    public int height;                              // height of the game screen

    /* Main Timer Data */
    private final int oneSec = 1000;                // 1s = 1000ms
    private Timer timer = null;                     // main timer, respond to trigger the refresh process of the whole project

    /* Player Data */
    public ReentrantLock p1_lk = new ReentrantLock(true);
    public Paddle paddle1 = null;             // bot paddle
    public Player player1 = null;
    public ReentrantLock p2_lk = new ReentrantLock(true);
    public Paddle paddle2 = null;             // top paddle if exists
    public Player player2 = null;

    /* Items Data */
    private ReentrantLock item_lk = new ReentrantLock(true);        // lock of item data
    private Map<Integer,Item> staticItem = new HashMap<>();         // static item registration, both Item and DynamicItem need to register here
    private Map<Integer,DynamicItem> dynamicItem = new HashMap<>(); // dynamic item registration, only DynamicItem need to register here
    private List<TimerObserver> timerObservers = new ArrayList<>();
    private List<Item> itemInQueue = new ArrayList<>();
    private List<TimerObserver> timerObserversInQueue = new ArrayList<>();

    /* Listener */
    private ControllerBeta controller = null;      // touch screen controller

    /* Barrel Eatable Support */
    public BarrelLine topLine;
    public BarrelLine botLine;

    private class MapChooser extends LinearLayout {
        private AppCompatActivity container = null;
        private Context context4map = null;
        private ScrollView parent = null;

        private List<String> mapList;

        private final Integer textColor = Color.RED;
        private final Float textSize = (float)30;
        private final Double figurewr = (double)2/3;
        private final Double figurehr = (double)1/4;
        private Integer figurew;
        private Integer figureh;

        private TextView popupName = null;
        private List<MapChooserController> mapChooserControllers = new ArrayList<>();
        private List<ImageButton> mapfigures = new ArrayList<>();

        public MapChooser(AppCompatActivity _container, Context _context4map, ScrollView _parent) {
            super(_container);

            container = _container;
            context4map = _context4map;
            parent = _parent;

            Double dtmp;
            dtmp = figurewr*width;
            figurew = dtmp.intValue();
            dtmp = figurehr*height;
            figureh = dtmp.intValue();

            setOrientation(LinearLayout.VERTICAL);
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

            popupName = new TextView(context4map);
            popupName.setTextColor(textColor);
            popupName.setTextSize(textSize);
            popupName.setText("Please Choose Down Side Map");
            addView(popupName);

            mapList = MapLoader.getMaps(container,context4map);

            for (int i = 0; i < mapList.size(); i++) {
                MapChooserController mcc;

                String thismap = mapList.get(i);

                Button mapButton = new Button(context4map);
                mapButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
                mapButton.setTextColor(textColor);
                mapButton.setTextSize(textSize);
                mapButton.setText(thismap);

                mcc = new MapChooserController(thismap,1,this);
                mapChooserControllers.add(mcc);
                mapButton.setOnClickListener(mcc);
                addView(mapButton);

                ImageButton mapFigure = new ImageButton(context4map);
                mapFigure.setImageResource(mapLoader.string2resid("map"+i+"_1","drawable"));
                mapFigure.setScaleType(ImageView.ScaleType.FIT_XY);
                mapFigure.setLayoutParams(new LayoutParams(figurew,figureh));
                mapfigures.add(mapFigure);

                mcc = new MapChooserController(thismap,1,this);
                mapChooserControllers.add(mcc);
                mapFigure.setOnClickListener(mcc);
                addView(mapFigure);
            }
        }

        public void setPosid(int posid) {
            if (posid != 2) Log.d("Model","MapChooser::setPosid(): ERROR! posid = " + posid);
            popupName.setText("Please Choose Top Side Map");
            for (int i = 0; i < mapChooserControllers.size(); i++)
                mapChooserControllers.get(i).setPosid(posid);
            for (int i = 0; i < mapfigures.size(); i++)
                mapfigures.get(i).setImageResource(mapLoader.string2resid("map"+i+"_2","drawable"));
            parent.fullScroll(ScrollView.FOCUS_UP);
        }

        private class MapChooserController implements View.OnClickListener {
            private String thismap = null;
            private Integer posid = -1;
            private MapChooser overload = null;

            public MapChooserController(String _thismap, Integer _posid, MapChooser _overload) {
                thismap = _thismap;
                posid = _posid;
                overload = _overload;
            }

            public void setPosid(int _posid) { posid = _posid; }

            @Override
            public void onClick(View v) {
                switch (posid) {
                    case 1:
                        mapFname1 = thismap;
                        overload.setPosid(2);
                        return;
                    case 2:
                        mapFname2 = thismap;
                        mapChooserPopup.dismiss();
                        mapChooserPopup.update();
                        loadMap();
                        return;
                    default:
                        Log.d("Model","MapChooser::MapChooserController: ERROR! posid = " + posid);
                        return;
                }
            }
        }
    }

    public void launchMapChooserPopup(AppCompatActivity _container, Context _context4map) {
        Log.d("Model","launchMapChooserPopup::start");

        ScrollView content = new ScrollView(_context4map);
        content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT));
        LinearLayout mapChooser = new MapChooser(_container,_context4map,content);
        content.addView(mapChooser);

        mapChooserPopup = new PopupWindow(_container);
        /* setWindowLayoutMode is not needed when using emulator, but due to some weird bug we have to have this to run on a real device*/
        mapChooserPopup.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mapChooserPopup.setContentView(content);
        mapChooserPopup.showAtLocation(this,Gravity.CENTER,0,0);
        mapChooserPopup.update();
    }

    /* Add item to model, capable for all class with Item as ancestor */
    public void addItem(Item item) {
        Log.d("Arknoid::Model", "Model::addItem(Item)::itemId = "+item.itemID);

        itemInQueue.add(item);
    }

    public void addItem() {
        boolean abort;
        int i;
        Item item;
        TimerObserver timerObserver;

        item_lk.lock();

        for (i = 0; i < itemInQueue.size(); i++) {
            item = itemInQueue.get(i);
            abort = false;

            if (staticItem.containsKey(item.itemID)) abort = true;
            if (item.isDynamic && dynamicItem.containsKey(item.itemID)) abort = true;

            if (!abort) {
                staticItem.put(item.itemID, item); // add to static item
                if (item.isDynamic) dynamicItem.put(item.itemID, (DynamicItem) item); // add to dynamic item
            }
        }

        itemInQueue.clear();

        for (i = 0; i < timerObserversInQueue.size(); i++) {
            timerObserver = timerObserversInQueue.get(i);
            abort = timerObservers.contains(timerObserver);

            if (!abort) {
                timerObservers.add(timerObserver);
            }
        }

        timerObserversInQueue.clear();

        item_lk.unlock();
    }

    public void addItem(TimerObserver timerObserver) {
        Log.d("Arknoid::Model", "Model::addItem(TimerObserver)::identifier = "+timerObserver.getIdentifier());

        timerObserversInQueue.add(timerObserver);
    }

    private void refreshObservers() {
        Log.d("Arknoid::Model", "Model::refreshObservers");

        List<DynamicItem> tmpDItems = new ArrayList<>();
        List<Item> tmpSItems = new ArrayList<>();

        item_lk.lock();

        tmpDItems.addAll(dynamicItem.values());
        tmpSItems.addAll(staticItem.values());

        Log.d("Arknoid::Model", "Model::refreshObservers()::timerObservers.size = "+timerObservers.size());

        for (int i = 0; i < timerObservers.size(); i++) {
            timerObservers.get(i).timerNotify(paddle1,paddle2,tmpDItems,tmpSItems);
        }

        item_lk.unlock();
    }

    /* Iterate thru each item and ask them to draw themselves */
    private void modelDraw(Canvas mCanvas) {
        Log.d("Arknoid::Model", "Model::modelDraw()");

        Item tmpItem = null;

        item_lk.lock();

        for (int key: staticItem.keySet()) {
            tmpItem = staticItem.get(key);
            if (sos) {
                String msg = "Model::modelDraw()::ADC = ";
                msg += tmpItem.activated ? "T" : "F";
                msg += tmpItem.isDynamic ? "T" : "F";
                msg += tmpItem.isChanged ? "T" : "F";
                Log.d("Arknoid::Model",msg);
            }
            // if (tmpItem.activated && (tmpItem.isDynamic || tmpItem.isChanged))
            if (tmpItem.activated)
                tmpItem.draw(mCanvas);
        }

        item_lk.unlock();
    }

    private void loadMap() {
        /* Load Same Map at Both Sides */
        // mapLoader.loadMap(mapFname,3);
        mapLoader.loadMap(mapFname1,1);
        mapLoader.loadMap(mapFname2,2);

        /* special bricks */
        topLine = (BarrelLine) itemFactory.createItem(ItemType.BARREL_LINE,0.0,13.0/180.0);
        topLine.activated = false;
        addItem(topLine);
        botLine = (BarrelLine) itemFactory.createItem(ItemType.BARREL_LINE,0.0,167.0/180.0);
        botLine.activated = false;
        addItem(botLine);

        /*Initialize Down Edge*/
        addItem(itemFactory.createItem(ItemType.DOWN_EDGE, 0.0, 0.9642));

        /*Initialize Upper Edge*/
        if (gameMode == Mode.P2S1 || gameMode == Mode.BLUETOOTH || gameMode == Mode.P1S1) addItem(itemFactory.createItem(ItemType.UPPER_EDGE, 0.0,0.0));

        /* Initialize Balls */
        addItem(itemFactory.createItem(ItemType.BASIC_BALL,(double)1/2,(double)2/3));
        addItem(itemFactory.createItem(ItemType.BASIC_BALL,(double)1/2,(double)1/3));

        /* Initialize Paddle1 */
        Item tmp = itemFactory.createItem(ItemType.BASIC_PADDLE,(double)1/2,(double)9/10);
        addItem(tmp);
        paddle1 = (Paddle) tmp;
        paddle1.setPaddle_type(paddleType);
        // controller.setP1(paddle1);

        /* Initialize Paddle2 if needed */
        if (gameMode == Mode.P2S1) {
            Log.d("Arknoid::Model", "Model::loadMap()::P2S1");
            Item tmp2 = itemFactory.createItem(ItemType.BASIC_PADDLE,(double)1/2,(double)1/10);
            addItem(tmp2);
            paddle2 = (Paddle) tmp2;
            paddle2.setPaddle_type(paddleType2);
            // controller.setP2(paddle2);
        } else if(gameMode == Mode.P1S1){//add AI paddle
            Item tmp2 = itemFactory.createItem(ItemType.BASIC_PADDLE,(double)1/2,(double)1/10);
            addItem(tmp2);
            addItem(new Paddle_Controller_AI(this,player2));
            paddle2 = (Paddle) tmp2;
            paddle2.setPaddle_type(paddleType2);
        }

        /* Initialize Controller */
        controller = new ControllerBeta(this);
        // controller = new ControllerRemote(this);
        // addItem((ControllerRemote)controller);
        setOnTouchListener(controller);

        maploaded = true;
    }

    /* This function is called by android UI when we need to update the view */
    @Override
    protected void onDraw(Canvas _canvas) {
        super.onDraw(_canvas);

        /* Background Color */
        /*
        Drawable d = getResources().getDrawable(R.drawable.black_background);
        d.setBounds(0,0,width+2,height+1);
        d.draw(_canvas); */
        _canvas.drawColor(Color.BLACK);

        Log.d("Arknoid::Model", "Model::onDraw()");

        modelDraw(_canvas);
    }

    /* This function is called when the View's size changed by android UI
     * At the beginning of the program, the size of view is 0x0
     * After the actual size is determined by android UI, this will be called
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h,oldw,oldh);

        Log.d("Arknoid::Model", "Model::onSizeChanged(int,int,int,int)");

        /* Update Model::width and Model::h */
        bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        width = w;
        height = h;

        canvas = new Canvas(bitmap);

        /* Start to run the game here so we can guarantee the UI initialization is done
         * We should start the game only once and we will enforce this later
         */
        //test();
        // loadMap();
        launchMapChooserPopup(mainActivity,context);
    }

    public Model(Context _context, AttributeSet _attributeSet) {
        super(_context,_attributeSet);

        Log.d("Arknoid::Model", "Model::Model_ctor");

        context = _context;

        randNum = new Random();
        randNum.setSeed(seed);

        itemFactory = new ItemFactory(this);
    }

    public void setVariables(AppCompatActivity _mainActivity, Mode _gameMode, int
            _paddleType,int _paddleType2) {
        Log.d("Arknoid::Model", "Model::setVariables(AppCompatActivity,Mode)");

        mainActivity = _mainActivity;

        gameMode = _gameMode;


        paddleType = _paddleType;
        paddleType2 = _paddleType2;
        if(_gameMode == Mode.CLASSIC){
            player1 = new Player(this,1,_paddleType);
            addItem(player1);
        } else{
            player1 = new Player(this,1,_paddleType);
            addItem(player1);
            player2 =new Player(this,0,_paddleType2);
            addItem(player2);
        }

        Log.d("Arknoid::Model", "Model::setVariables(AppCompatActivity,Mode):: gameMode = "+gameMode.toString());

        mapLoader = new MapLoader(this);
    }

    /* This function is designed to remove the given item from the model
     * Should be used when we decide to destruct an item "model.popItem(this);"
     * For now we do not use this since we have activate system (item may stay in model as zombie)
     * This should be used when we wish to optimize the program
     * This function is not finished yet
     */
    public Boolean popItem(Item item) {
        Boolean retval = false;

        Log.d("Arknoid::Model", "Model::popItem(Item)::itemId = "+item.itemID);

        item_lk.lock();

        if (staticItem.containsKey(item.itemID)) {
            staticItem.remove(item.itemID);
            retval = true;
        }

        item_lk.unlock();

        return retval;
    }

    /* Disable main timer (pause the game) */
    public void pauseTimer() {
        Log.d("Arknoid::Model","Model::pauseTimer()");

        if (timer == null) {
            Log.d("Arknoid:Model","Model::pauseTimer()::ERROR! Timer already paused!");
            return;
        }

        timer.cancel();
        timer.purge();
        timer = null;
    }

    /* Enable main timer (start/resume the game) */
    public void setTimer() {
        Log.d("Arknoid::Model", "Model::setTimer()");

        if (timer != null) {
            Log.d("Arknoid:Model","Model::setTimer()::ERROR! Timer already exists!");
            return;
        }

        /* Initialize Timer (it runs in another thread) */
        timer = new Timer();

        /* Assign timer Task, which will be ran on the timer thread every (oneSec/fps) ms */
        timer.schedule(new TimerTask(){
            public void run() {
                refresh();
            }
        },oneSec/fps,oneSec/fps);
    }



    public void refresh() {
        Log.d("Arknoid::Model", "Model::refresh()");

        Item tmpItem = null;
        DynamicItem tmpDynamicItem = null;
        int bricknum = 0;

        /* Check if run out of bricks */
        item_lk.lock();
        for (int i: staticItem.keySet()) {
            tmpItem = staticItem.get(i);
            if (tmpItem.activated && ItemFactory.isBrick(tmpItem.type)) bricknum++;
        }
        if (maploaded && bricknum == 0) {
            mapLoader.loadMap(mapFname1,1);
            mapLoader.loadMap(mapFname2,2);
        }
        item_lk.unlock();

        /* Refresh Controller */
        if (controller != null) controller.timerNotify();

        /* Forward Adding Queue */
        addItem();

        /* Refresh TimerObservers */
        refreshObservers();

        item_lk.lock();

        /* Calling Move */
        for (int i: dynamicItem.keySet()) {
            tmpDynamicItem = dynamicItem.get(i);
            if (!tmpDynamicItem.activated) continue;
            tmpDynamicItem.move();
        }

        /* Calling Checkhit */
        for (int i: dynamicItem.keySet()) {
            tmpDynamicItem = dynamicItem.get(i);
            if (!tmpDynamicItem.activated) continue;
            for (int j: staticItem.keySet()) {
                tmpItem = staticItem.get(j);
                if (!tmpItem.activated) continue;
                if (tmpDynamicItem.returnID() == tmpItem.returnID())
                    continue;
                tmpDynamicItem.checkHit(tmpItem);
            }
        }

        item_lk.unlock();

        /* Calling Draw */
        mainActivity.runOnUiThread(new Runnable() { // note refresh() is triggered by and run on timer thread
            @Override
            public void run() {
                invalidate(); // invalidate the View (canvas) such that onDraw will be called by android UI in future
            }
        });
    }

    public Mode getMode() {
        return gameMode;
    }

    public Player getPlayer1(){
        return  player1;
    }
    public  Player getPlayer2(){
        return player2;
    }

    public int getNum () {
        return randNum.nextInt(99);
    }

    public AppCompatActivity getMainActivity() { return mainActivity; }

    public Context getMainContext() { return context; }
    
}
