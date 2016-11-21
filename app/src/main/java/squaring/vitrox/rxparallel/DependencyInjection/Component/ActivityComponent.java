package squaring.vitrox.rxparallel.DependencyInjection.Component;

import dagger.Component;
import squaring.vitrox.rxparallel.DependencyInjection.ActivityScope;
import squaring.vitrox.rxparallel.MainActivity;


@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent extends AppComponent{

    void inject(MainActivity activity);

}