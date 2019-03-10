package com.allanweber.woopsicredi.domain.entity;

import com.allanweber.woopsicredi.domain.exception.UserAreadyVotedException;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class VotingTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldCompareEqualsVoting(){

        Ruling ruling = new Ruling( "name");
        Voting voting1 = new Voting(ruling, 1);
        Voting voting2 = new Voting(ruling, 1);
        assertEquals(voting1, voting2);

        ReflectionTestUtils.setField(voting2, "expiration", 2);
        assertNotEquals(voting1, voting2);
    }

    @Test
    public void shouldValidateNotExpired() throws InterruptedException {
        Date creation = DateUtils.addMinutes(new Date(), -2);
        Ruling ruling = new Ruling( "name");
        Voting voting = new Voting(ruling, 1);
        assertFalse(voting.isExpired());
    }

    @Test
    public void shouldValidateExpired() throws InterruptedException {
        Date creation = new Date();
        Ruling ruling = new Ruling( "name");
        Voting voting = new Voting(ruling, 1);
        ReflectionTestUtils.setField(voting, "expirationDate", DateUtils.addMinutes(new Date(), -2));
        assertTrue(voting.isExpired());
    }

    @Test
    public void shouldAddVote(){
        Date creation = new Date();
        Ruling ruling = new Ruling( "name");
        Voting voting = new Voting(ruling,  1);

        Vote vote1 = new Vote("123", Answer.SIM);
        Vote vote2 = new Vote("546", Answer.SIM);

        voting.addVote(vote1);
        voting.addVote(vote2);

        assertEquals(2, voting.getVotes().size());
    }

    @Test
    public void shouldNotAddEqualVote(){
        expectedException.expectMessage("The user id '123' have already voted for this voting 'name'");
        expectedException.expect(UserAreadyVotedException.class);

        Date creation = new Date();
        Ruling ruling = new Ruling( "name");
        Voting voting = new Voting(ruling, 1);

        Vote vote1 = new Vote("123", Answer.SIM);
        Vote vote2 = new Vote("123", Answer.NAO);
        Vote vote3 = new Vote("456", Answer.NAO);

        voting.addVote(vote1);
        voting.addVote(vote2);
        voting.addVote(vote3);
    }

    private Date getDate(String dateString) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

}