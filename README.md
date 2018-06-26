# LifeCycleProject
android LifeCycle 使用说明
可以查看个人简书：https://www.jianshu.com/p/5729d537f263
### 前言
LifeCycle是Android官方提供的组件，可见地址：https://developer.android.com/topic/libraries/architecture/lifecycle

Lifecycle 是一个类，它持有关于组件（如 Activity 或 Fragment）生命周期状态的信息，并且允许其他对象观察此状态。

### 以下是阅读官网的说明：
LifeCycle指的是使用生命周期通知组件去处理生命周期。

支持生命周期的组件执行操作以响应另一个组件（例如活动和片段）的生命周期状态更改。这些组件可帮助您生成组织性更好，并且通常重量更轻的代码，这些代码更易于维护。

常见的模式是在activity和fragment的生命周期方法中实现依赖组件的操作。但是，这种模式导致代码的组织不良以及错误泛滥。通过使用生命周期感知组件，您可以将相关组件的代码从生命周期方法中移出并移入组件本身。

该android.arch.lifecycle 软件包提供了类和接口，使您可以构建支持生命周期的 组件，这些组件可以根据activity或fragment的当前生命周期状态自动调整其行为。

### 注意：要导入 android.arch.lifecycle 到您的Android项目中

### 为什么要用LifeCycle
我们在处理activity或者fragment的生命周期时，不可避免会遇到这样的情况：在activity的oncreate中初始化某些成员变量，在onstop中释放或者销毁。实际开发中会导致oncreate onstop中的代码非常臃肿。

### LifeCycle使用说明
## 举例使用mvp模式说明：

### 1、新建工程
在project的build.gradle中添加

maven {

            url 'https://maven.google.com'

        }

官网上写的是google()，但是google()需要gradle 4.0才支持

### 2、要观察到Presenter的生命周期事件都列了出来，封装到BasePresenter 中，只要继承了BasePresenter 的子类都能感知到Activity容器对应的生命周期事件，并在子类重写对应的行为代码

public interface IPresenter extends LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)

    void onCreate(@NotNull LifecycleOwner owner);

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)

    void onDestory(@NotNull LifecycleOwner owner);

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)

    void onLifeCycleChanged(@NotNull LifecycleOwner owner, @NotNull Lifecycle.Event event);

}






public class BasePresenter implements IPresenter {



    @Override

    public void onCreate(@NotNull LifecycleOwner owner) {

    }

    @Override

    public void onDestory(@NotNull LifecycleOwner owner) {

    }

    @Override

    public void onLifeCycleChanged(@NotNull LifecycleOwner owner, @NotNull Lifecycle.Event event) {

    }

}




public class MainPresenter extends BasePresenter {

    private final String TAG = MainPresenter.class.getName();

    private Context context;

    public MainPresenter(Context context) {

        this.context = context;

    }

    @Override

    public void onCreate(@NotNull LifecycleOwner owner) {

        super.onCreate(owner);

        Log.d(TAG, "LifeCycle------onCreate------");

    }

    @Override

    public void onDestory(@NotNull LifecycleOwner owner) {

        super.onDestory(owner);

        Log.d(TAG, "LifeCycle------onDestory------");

    }

}

### 3、在Activity/Fragment容器中添加Observer：



public class MainActivity extends AppCompatActivity {

    private IPresenter mPresenter;

    private final String TAG = MainActivity.class.getName();

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity------onCreate------");

        mPresenter = new MainPresenter(this);

        getLifecycle().addObserver(mPresenter);//添加LifecycleObserver

    }

    @Override

    protected void onDestroy() {

        super.onDestroy();

        Log.d(TAG, "MainActivity------onDestroy------");

    }

}

这样的话，每当Activity发生了对应的生命周期改变，Presenter就会执行对应事件注解的方法

除onCreate和onDestroy事件之外，Lifecycle一共提供了所有的生命周期事件，只要 

通过注解进行声明，就能够使LifecycleObserver观察到对应的生命周期事件：





### 原理说明


LifecycleObserver接口（ Lifecycle观察者）：实现该接口的类，通过注解的方式，可以通过被LifecycleOwner类的addObserver(LifecycleObserver o)方法注册,被注册后，LifecycleObserver便可以观察到LifecycleOwner的生命周期事件。

LifecycleOwner接口（Lifecycle持有者）：实现该接口的类持有生命周期(Lifecycle对象)，该接口的生命周期(Lifecycle对象)的改变会被其注册的观察者LifecycleObserver观察到并触发其对应的事件。

Lifecycle(生命周期)：和LifecycleOwner不同的是，LifecycleOwner本身持有Lifecycle对象，LifecycleOwner通过其Lifecycle getLifecycle()的接口获取内部Lifecycle对象。

State(当前生命周期所处状态)：如图所示。

Event(当前生命周期改变对应的事件)：如图所示，当Lifecycle发生改变，如进入onCreate,会自动发出ON_CREATE事件。


以SupportActivity（AppCompatActivity extends SupportActivity）为例，SupportActivity实现了LifecycleOwner接口，这意味着SupportActivity对象持有生命周期对象（Lifecycle），并可以通过Lifecycle getLifecycle()方法获取内部的Lifecycle对象


可以看到，实现的getLifecycle()方法，实际上返回的是 LifecycleRegistry 对象，LifecycleRegistry对象实际上继承了 Lifecycle，这个下文再讲。

持有Lifecycle有什么作用呢？实际上在SupportActivity（ ReportFragment.injectIfNeededIn()）对应的生命周期内，都会发送对应的生命周期事件给内部的 LifecycleRegistry对象处理，内部的Lifecycle对象（就是mLifecycleRegistry）将生命周期对应的事件作为参数传给了 handleLifecycleEvent() 方法


handleLifecycleEvent方法会通过 getStateAfter 获取当前应处的状态并修改 Lifecycle本身的State 值，紧接着遍历所 LifecycleObserver 并同步且通知其状态发生变化，因此就能触发LifecycleObserver 对应的生命周期事件。



### 总结
Lifecycle简单且独立（实际上配合其他组件味道更佳）。

