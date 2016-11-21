# RXparallelism
This project shows a simple way to implement REST API CALLS in parallel using RXJAVA the project uses also Dagger2 as way of implementation
for this case is sed the GITHUB API,
first of all we get 2 random users from github and then for each users we are going to get the repositories and the user details, 
on Android Studio you can see a more detailed times for each call, but in general in the inteface we are getting the ending time in Milliseconds when all the data is getted for each user.
for a RXJAVA there's problem (because is not thinked as parallel structure) 
in some cases the doOnComplete is not entering because the observable callback for this method relased when one of them is finished even if the operations on call method are done.
more info about this here:https://github.com/ReactiveX/RxScala/issues/145
