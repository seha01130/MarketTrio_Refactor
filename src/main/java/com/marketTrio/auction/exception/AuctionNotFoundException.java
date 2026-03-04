package com.marketTrio.auction.exception;

public class AuctionNotFoundException extends RuntimeException {

	private final int auctionId;

	public AuctionNotFoundException(int auctionId) {
		super("Auction not found with id: " + auctionId);
		this.auctionId = auctionId;
	}

	public int getAuctionId() {
		return auctionId;
	}
}

