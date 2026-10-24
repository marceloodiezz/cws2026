package uo.ri.cws.application.ui.util;

import java.util.List;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.invoice.InvoicingService.PaymentMeanDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService.VehicleTypeDto;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService.WorkOrderDto;
import uo.ri.util.console.Console;

public class Printer {


	public static void printInvoice(InvoiceDto invoice) {
	    Console.printf("Invoice number: %d%n", invoice.number);
	    Console.printf("\tDate: %1$td/%1$tm/%1$tY%n", invoice.date);
	    Console.printf("\tAmount: %.2f €%n", invoice.subtotal);
	    Console.printf("\tVAT: %.1f %%%n", invoice.vatPercentage);
	    Console.printf("\tTotal (including VAT): %.2f €%n", invoice.total);
	}
	
    public static void printInvoicingWorkOrder(InvoicingWorkOrderDto wo) {
        Console.printf("\t%s \t%-40.40s \t%s \t%-12.12s \t%.2f%n"
                , wo.id
                , wo.description
                , wo.date
                , wo.state
                , wo.amount
        );
    }
    
	public static void printPaymentMeans(List<PaymentMeanDto> medios) {
		Console.println();
		Console.println("Available payment means");

		Console.printf("\t%s \t%-8.8s \t%s \n", "Id", "Type", "Acummulated");
		for (PaymentMeanDto medio : medios) {
			printPaymentMean( medio );
		}
	}

	private static void printPaymentMean(PaymentMeanDto medio) {
		Console.printf("\t%s \t%-8.8s \t%s \n"
				, medio.id
				, medio.getClass().getName()  // not the best...
				, medio.accumulated
		);
	}

	public static void printWorkOrder(WorkOrderDto rep) {

		Console.printf("\t%s \t%-40.40s \t%td/%<tm/%<tY \t%-12.12s \t%.2f\n"
				, rep.id
				, rep.description
				, rep.date
				, rep.state
				, rep.amount
		);
	}

	public static void printMechanic(MechanicDto m) {

		Console.printf("\t%s %-10.10s %-15.15s %-25.25s\n"
				, m.id
				, m.nif
				, m.name
				, m.surname
			);
	}

	public static void printVehicleType(VehicleTypeDto vt) {

		Console.printf("\t%s %-10.10s %5.2f €/hour %d\n"
				, vt.id
				, vt.name
				, vt.pricePerHour
				, vt.minTrainigHours
			);
	}


	public static void printWorkOrderDetail(WorkOrderDto wo) {

		Console.printf("%s for vehicle %s\n\t%-25.25s\n\t%tm/%<td/%<tY\n\t%s\n",
				wo.id
				, wo.vehicleId
				, wo.description
				, wo.date
				, wo.state
			);
	}
	

    public static void printPaymentMean(
            uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto dto) {
        
        Console.println();
        Console.printf("Id: %s%n", dto.id);
        Console.printf("Client id: %s%n", dto.clientId);
        Console.printf("Version: %d%n", dto.version);
        Console.printf("Accumulated amount: %.2f EUR%n", dto.accumulatedAmount);
        
        if (dto instanceof CardDto) {
            CardDto card = (CardDto) dto;
            
            Console.println("Type: Credit card");
            Console.printf("Number: %s%n", card.cardNumber);
            Console.printf("Card type: %s%n", card.cardType);
            Console.printf("Expiration: %s%n", card.cardExpiration);
        }
        else if (dto instanceof VoucherDto) {
            VoucherDto voucher = (VoucherDto) dto;
            
            Console.println("Type: Voucher");
            Console.printf("Code: %s%n", voucher.code);
            Console.printf("Description: %s%n", voucher.description);
            Console.printf("Available amount: %.2f EUR%n", voucher.availableAmount);
        }
        else {
            Console.println("Type: Cash");
        }
    }
    

    public static void printVoucher(
            uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto voucher) {
        
        Console.println();
        Console.printf("Id: %s%n", voucher.id);
        Console.printf("Code: %s%n", voucher.code);
        Console.printf("Description: %s%n", voucher.description);
        Console.printf("Version: %d%n", voucher.version);
        Console.printf("Available amount: %.2f EUR%n", voucher.availableAmount);
        Console.printf("Accumulated amount: %.2f EUR%n", voucher.accumulatedAmount);
    }
    

    public static void printVoucherSummary(VoucherSummaryDto summary) {
        Console.println();
        Console.printf("Client: %s %s%n", summary.name(), summary.surname());
        Console.printf("NIF: %s%n", summary.nif());
        Console.printf("Issued vouchers: %d%n", summary.issuedQty());
        Console.printf("Total amount: %.2f EUR%n", summary.totalAmount());
        Console.printf("Available amount: %.2f EUR%n", summary.availableAmount());
        Console.printf("Consumed amount: %.2f EUR%n", summary.consumedAmount());
    }




	

}