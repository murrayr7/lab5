package poker;

import java.util.ArrayList;
import java.util.Collections;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

public class PlayHand {
	private static SessionFactory factory; 

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		try{

			factory = new AnnotationConfiguration().configure().addAnnotatedClass(Hand.class).buildSessionFactory();

		}catch (Throwable ex) { 
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex); 
		}

		for (int gCount = 0; gCount <= 200000; gCount++) {
			ArrayList<Hand> Hands = new ArrayList<Hand>();
			Deck d = new Deck();

			for (int hCnt = 0; hCnt <= 2; hCnt++) {
				Session session = factory.openSession();
				Transaction tx = null;
				Integer seqnum = null;
				try{
					tx = session.beginTransaction();
					Hand h = new Hand(d);
					h.EvalHand();
					seqnum = (Integer) session.save(h); 
					tx.commit();
					//System.out.println("The transaction has completed without error (seqnum:" + seqnum + ")");
					System.out.print(" "+seqnum+" ");
					Hands.add(h);
				}catch (HibernateException e) {
					if (tx!=null) tx.rollback();
					e.printStackTrace(); 
				}finally {
					session.close(); 
				}
			}
			Collections.sort(Hands, Hand.HandRank);
			
			/*
			System.out
			.print("Hand Strength: " + Hands.get(0).getHandStrength());
			System.out.print(" Hi Hand: " + Hands.get(0).getHighPairStrength());
			System.out.print(" Lo Hand: " + Hands.get(0).getLowPairStrength());
			System.out.print(" Kicker: " + Hands.get(0).getKicker());

			System.out.print(" beats ");

			System.out.print(" Hand Strength: "
					+ Hands.get(1).getHandStrength());
			System.out.print(" Hi Hand: " + Hands.get(1).getHighPairStrength());
			System.out.print(" Lo Hand: " + Hands.get(1).getLowPairStrength());
			System.out.print(" Kicker: " + Hands.get(1).getKicker());

			System.out.print("\n");
			*/


		}

	}

}
