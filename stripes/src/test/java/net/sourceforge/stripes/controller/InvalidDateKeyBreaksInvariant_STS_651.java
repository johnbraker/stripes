package net.sourceforge.stripes.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.sourceforge.stripes.FilterEnabledTestBase;
import net.sourceforge.stripes.mock.MockRoundtrip;


/**
 * reproduces http://www.stripesframework.org/jira/browse/STS-651
 */
public class InvalidDateKeyBreaksInvariant_STS_651 extends FilterEnabledTestBase {

   @Test
   public void bindInvalidDateKeysInMapBreaksMapInvariant() throws Exception {
      MockRoundtrip trip = getRoundtrip();
      trip.addParameter("mapDateDate['notadate']", "01/01/2000");
      trip.execute();

      MapBindingTests bean = trip.getActionBean(MapBindingTests.class);
      Map<Date, Date> mapDateDate = bean.getMapDateDate();
      try {
         // there should be only java.util.Date objects as keys of the map
         for ( Date dateKey : mapDateDate.keySet() ) {
            // if we go this far then it's ok, but we try to see
            // if the value if ok as well...
            Date dateValue = mapDateDate.get(dateKey);
            assertThat(dateValue).isNotNull();
         }
      }
      catch ( ClassCastException e ) {
         fail("bad ! Map<Date,Date> contains a <String,?> entry, the map's invariant has been violated", e);
      }
   }

   /** Helper method to create a roundtrip with the TestActionBean class. */
   protected MockRoundtrip getRoundtrip() {
      return new MockRoundtrip(getMockServletContext(), MapBindingTests.class);
   }
}
