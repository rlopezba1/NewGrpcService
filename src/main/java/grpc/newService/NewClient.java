package grpc.newService;

import java.util.concurrent.TimeUnit;

import grpc.newService.newServiceGrpc.newServiceBlockingStub;
import grpc.newService.newServiceGrpc.newServiceStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class NewClient {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		//1. create a channel
		//2.create a message
		//3.create a stub
		
		//Build a channel - a channel connect the client to the server
		int port = 50051;
		String host = "localhost";
		
		ManagedChannel newChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		
		//now build our message
		containsString cString = containsString.newBuilder().setFirstString("test").build();
		
		//create a stub
		// the stub is a local representation of our remote object or service		
		newServiceBlockingStub bstub = newServiceGrpc.newBlockingStub(newChannel);
		newServiceStub asyncstub = newServiceGrpc.newStub(newChannel);

		
		containsString response = bstub.getFirsttString(cString);
		
		//printout the response
		System.out.println("This is the client: " + response.getFirstString());
		//// Client Streaming Code ////
		// for incomig messages we need to implement a StreamObserver
		// then we pass it back to the grpc library - which will then use it on our behalf
		
		StreamObserver<containsString> responseObserver = new StreamObserver<containsString>() {

			@Override
			public void onNext(containsString value) {
				// TODO Auto-generated method stub
				System.out.println("The response from the server is : " + value.getFirstString());
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				
			}			
		};
		
		// grpc library returns a streamObserver to us: requestObserver
		// we use this to out outgoing messages
		StreamObserver<containsString> requestObserver = asyncstub.sendStringClientStreaming(responseObserver);

		requestObserver.onNext(containsString.newBuilder().setFirstString("First message in the stream from the client").build());
		requestObserver.onNext(containsString.newBuilder().setFirstString("Second message in the stream from the client").build());
		requestObserver.onNext(containsString.newBuilder().setFirstString("Third message in the stream from the client").build());
		
		requestObserver.onCompleted();
		Thread.sleep(5000);
		
		newChannel.shutdown().awaitTermination(5,TimeUnit.SECONDS );		

	}

}
