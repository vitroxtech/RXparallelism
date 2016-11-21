package squaring.vitrox.rxparallel;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.inject.Inject;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import squaring.vitrox.rxparallel.Adapter.ListAdapter;
import squaring.vitrox.rxparallel.Adapter.SectionedListAdapter;
import squaring.vitrox.rxparallel.Model.GithubFullResponse;
import squaring.vitrox.rxparallel.Model.GithubRepository;
import squaring.vitrox.rxparallel.Model.GithubUser;
import squaring.vitrox.rxparallel.Model.SearchUserResult;
import squaring.vitrox.rxparallel.Network.GitApiService;

public class MainActivity extends BaseActivity {

    @Inject
    GitApiService mService;

    List<SectionedListAdapter.Section> sections;
    RecyclerView mRecyclerView;
    ListAdapter itemListAdapter;
    LinearLayout comparatorLayout;
    TextView repo1Name;
    TextView repo2Name;
    TextView duration1;
    TextView duration2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getComponent().inject(this);
        comparatorLayout=(LinearLayout) findViewById(R.id.paragonLayout);
        repo1Name= (TextView) findViewById(R.id.repoName);
        repo2Name=(TextView) findViewById(R.id.repoName2);
        duration1=(TextView) findViewById(R.id.totalDuration);
        duration2=(TextView) findViewById(R.id.totalDuration2);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        Button goButton = (Button) findViewById(R.id.search_button);
        itemListAdapter = new ListAdapter(this);
        sections= new ArrayList<>();
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.getListUsers(getRandomIntInRange(1, 50)).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread())
                        .subscribe(new Observer<List<SearchUserResult>>() {
                            @Override
                            public void onCompleted() {}
                            @Override
                            public void onError(Throwable e) {
                                SendErrorMessage(e.getMessage());
                            }

                            @Override
                            public void onNext(List<SearchUserResult> users) {
                                makeParallelism(users);
                            }
                        });
            }

        });

    }
    public void makeParallelism(List<SearchUserResult> users) {
        final long startTime = System.currentTimeMillis();
        String id1 = users.get(getRandomIntInRange(0, users.size() - 1)).getLogin();
        String id2 = users.get(getRandomIntInRange(0, users.size() - 1)).getLogin();
        final Map<String,String> mymap= new HashMap<>();

        Observable<GithubFullResponse> myresponse = mockClient(id1, id2).flatMap
                (new Func1<String, Observable<GithubFullResponse>>() {
                    @Override
                    public Observable<GithubFullResponse> call(final String t) {

                        final Observable<List<GithubRepository>> repos = mService.getUserRepos(t).doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                logTime("gettingReposOf[" + t + "] subscribing ", startTime);
                            }
                        })
                                .doOnCompleted(new Action0() {
                                    @Override
                                    public void call() {
                                        logTime("gettingReposOf[" + t + "] completed ", startTime);
                                    }
                                }).subscribeOn(Schedulers.io());

                        final Observable<GithubUser> details = mService.getUserDetail(t).doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                logTime("userdetail[" + t + "] subscribing ", startTime);
                            }
                        })
                                .doOnCompleted(new Action0() {
                                    @Override
                                    public void call() {
                                        logTime("userdetail[" + t + "] completed ", startTime);
                                    }
                                }).subscribeOn(Schedulers.io());

                        return Observable.zip(details, repos, new Func2<GithubUser, List<GithubRepository>, GithubFullResponse>() {
                            @Override
                            public GithubFullResponse call(GithubUser r, List<GithubRepository> u) {
                                return new GithubFullResponse(t, r, u);
                            }
                        }).doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                mymap.put(t,String.valueOf(System.currentTimeMillis() - startTime));
                                logTime("zip[" + t + "] completed ", startTime);
                            }
                        });
                    }
                });
        List<GithubFullResponse> allTiles = myresponse.toList().doOnCompleted(new Action0() {
            @Override
            public void call() {
                logTime("All Tiles Completed ", startTime);
            }
        }).toBlocking().single();
        OrderAndSetupView(allTiles,mymap);

    }

    private <T> Observable<T> mockClient(final T... ts) {
        return Observable.create(new Observable.OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> s) {
                for (T t : ts) {
                    s.onNext(t);
                }
                s.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
        // the use of subscribeOn to make an otherwise synchronous Observable async
    }

    private static void logTime(String message, long startTime) {
        System.out.println(message + " => " + (System.currentTimeMillis() - startTime) + "ms");
    }

    public int getRandomIntInRange(int min, int max) {
        Random mRandom = new Random();
        int solve = mRandom.nextInt((max - min) + min) + min;
        System.out.println("random: " + solve);
        return solve;
    }

    private void SendErrorMessage(final String txt) {
        Snackbar.make(findViewById(android.R.id.content), txt, Snackbar.LENGTH_LONG).show();
    }

    public void OrderAndSetupView(List<GithubFullResponse> dataObjList, Map<String,String> durations)
    {
        itemListAdapter.clear();
        sections.clear();
        int totalrows=0;
        for(int i=0; i<dataObjList.size();i++)
        {
            sections.add(new SectionedListAdapter.Section(i+totalrows,dataObjList.get(i).getUser()));
            List<GithubRepository> repos=dataObjList.get(i).getRepositories();
            for(int j=0; j<repos.size();j++)
            {
                itemListAdapter.addData(repos.get(j));
                totalrows++;
            }
            //balance the value because the j starts in 0
            totalrows--;
        }
        SectionedListAdapter.Section[] dumb = new SectionedListAdapter.Section[sections.size()];
        SectionedListAdapter mSectionedAdapter = new
                SectionedListAdapter(this,R.layout.item_header,R.id.headerTitle,itemListAdapter);
        mSectionedAdapter.setSections(sections.toArray(dumb));
        //Apply this adapter to the RecyclerView then SHOW! :)
        mRecyclerView.setAdapter(mSectionedAdapter);
        //Order the other view
        String user1= dataObjList.get(0).getUser().getLogin();
        String user2= dataObjList.get(1).getUser().getLogin();
        String time1= durations.get(user1);
        String time2= durations.get(user2);
        repo1Name.setText(user1);
        repo2Name.setText(user2);
        duration1.setText(String.format("Time: %s ms", time1));
        duration2.setText(String.format("Time %s ms", time2));
        comparatorLayout.setVisibility(View.VISIBLE);
    }


}
