package utils;

public class Pair<P, K> {
	P v1;
	K v2;

	public Pair(P v1, K v2) {
		super();
		this.v1 = v1;
		this.v2 = v2;
	}

	public P getV1() {
		return v1;
	}

	public void setV1(P v1) {
		this.v1 = v1;
	}

	public K getV2() {
		return v2;
	}

	public void setV2(K v2) {
		this.v2 = v2;
	}

	@Override
	public int hashCode() {
		return v1.hashCode() + v2.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!Pair.class.isInstance(obj))
			return false;
		try {
			@SuppressWarnings("unchecked")
			Pair<P, K> pObj = (Pair<P, K>) obj;
			return this.v1.equals(pObj.getV1()) && this.v2.equals(pObj.getV2());
		} catch (ClassCastException e) {
			return false;
		}
	}
}
